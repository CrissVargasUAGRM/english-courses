package org.example;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class PinBlockGenerator4 {

    public static void main(String[] args) {
        try {
            // Datos de entrada
            String pin = "5363";  // PIN de 4 dígitos
            String pan = "4218289900000018";  // PAN (número de tarjeta de 16 dígitos)
            String zpk = "2315208C9110AD4004DA0119C8025132";  // ZPK (clave de 32 caracteres hexadecimales)
            char filler = 'F';  // Caracter de relleno (filler), 'F' o '0'

            // Verificar y obtener la clave en formato adecuado para Triple DES (DESede)
            SecretKeySpec key = getKey(zpk);

            // Generar el PIN Block cifrado
            String pinBlock = generatePinBlock(pin, pan, key, filler);

            // Imprimir el PIN Block generado en formato hexadecimal
            System.out.println("PIN Block generado: " + pinBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para generar la clave en formato adecuado para DESede (Triple DES)
    public static SecretKeySpec getKey(String zpk) throws Exception {
        if (zpk.length() == 32) {
            // Si la clave tiene 32 caracteres, la duplicamos para completar los 48 caracteres
            zpk = zpk + zpk.substring(0, 16);  // Duplicar los primeros 16 caracteres
        }

        if (zpk.length() != 48) {
            throw new Exception("La clave ZPK debe tener 48 caracteres hexadecimales.");
        }

        // Convertir la clave ZPK en un arreglo de 24 bytes
        byte[] keyBytes = new byte[24]; // 24 bytes para DESede (Triple DES)
        for (int i = 0; i < 24; i++) {
            int index = i * 2;
            int value = Integer.parseInt(zpk.substring(index, index + 2), 16);
            keyBytes[i] = (byte) value;
        }

        // Devolver la clave como un SecretKeySpec para Triple DES (DESede)
        return new SecretKeySpec(keyBytes, "DESede");
    }

    // Método para generar el PIN Block a partir de un PIN, PAN y una clave ZPK
    public static String generatePinBlock(String pin, String pan, SecretKeySpec key, char filler) throws Exception {
        // Construir el primer bloque del PIN Block
        String pinBlockPartOne = "0" + String.format("%1$X", pin.length()) + pin;
        int length = pinBlockPartOne.length();
        if (length < 16) {
            // Si el PIN Block tiene menos de 16 caracteres, rellenar con 'F' o '0' según se indique
            while (pinBlockPartOne.length() < 16) {
                pinBlockPartOne += filler;
            }
        }

        // Verificar que el primer bloque tenga 16 caracteres
        if (pinBlockPartOne.length() != 16) {
            throw new Exception("El primer bloque de PIN debe tener 16 caracteres.");
        }

        // Construir el segundo bloque del PIN Block a partir del PAN
        String panBlockPartTwo = "0000" + pan.substring(3, pan.length() - 1);

        // Verificar que el segundo bloque tenga 16 caracteres
        if (panBlockPartTwo.length() != 16) {
            throw new Exception("El segundo bloque del PAN debe tener 16 caracteres.");
        }

        // Mostrar los dos bloques
        System.out.println("PIN Block Part One: " + pinBlockPartOne);
        System.out.println("PIN Block Part Two: " + panBlockPartTwo);

        // Realizar la operación XOR entre los dos bloques
        byte[] block1 = hexStringToByteArray(pinBlockPartOne);
        byte[] block2 = hexStringToByteArray(panBlockPartTwo);
        byte[] xorResult = xorByteArrays(block1, block2);

        // Mostrar el resultado del XOR
        System.out.println("Resultado XOR (Hex): " + bytesToHex(xorResult));

        // Cifrar el resultado XOR utilizando 3DES con CBC (sin padding)
        byte[] encryptedPinBlock = tripleDESEncryptWithCBC(xorResult, key);

        // Mostrar el PIN Block cifrado
        System.out.println("PIN Block cifrado (Hex): " + bytesToHex(encryptedPinBlock));

        // Retornar el PIN Block cifrado en formato hexadecimal
        return bytesToHex(encryptedPinBlock);
    }

    // Método para realizar XOR entre dos arreglos de bytes
    public static byte[] xorByteArrays(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length];
        for (int i = 0; i < array1.length; i++) {
            result[i] = (byte) (array1[i] ^ array2[i]);
        }
        return result;
    }

    // Método para cifrar usando Triple DES con CBC (sin padding)
    public static byte[] tripleDESEncryptWithCBC(byte[] data, SecretKeySpec key) throws Exception {
        // La inicialización del vector (IV) debe ser proporcionada. Aquí usamos un IV de ceros.
        byte[] iv = new byte[8]; // Vector de inicialización de 8 bytes
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return cipher.doFinal(data);
    }

    // Método para convertir un arreglo de bytes a su representación hexadecimal
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    // Método para convertir una cadena hexadecimal a un arreglo de bytes
    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}

