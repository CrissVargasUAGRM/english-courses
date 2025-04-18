@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(a -> a.requestMatchers(HttpMethod.GET, "/productsapi/products/{id:^[0-9]*$}", "/showGetProduct", "/getProduct").hasAnyRole("USER", "ADMIN")
        .requestMatchers(HttpMethod.GET, "/showCreateProduct", "/createProduct", "/createResponse" )
        .hasAnyRole("ADMIN").requestMatchers(HttpMethod.POST, "/getProduct").hasAnyRole("ADMIN", "USER")
        .requestMatchers(HttpMethod.POST, "/productsapi/products/**").hasAnyRole("ADMIN")
        .requestMatchers("/", "/login").permitAll());
		http.csrf(c -> c.disable());
		return http.build();	
}


@Controllerpublic class ProductController {

	@Autowired	private ProductRepo repo;

	@GetMapping("/showCreateProduct")	
    public String showCreateProduct() {		
        return "createProduct";	
    }

	@PostMapping("/saveProduct")	
    public String save(Product product) {		
        repo.save(product);		
        return "createResponse";	
    }

	@GetMapping("/showGetProduct")	
    public String showGetProduct() {		
        return "getProduct";	
    }

	@PostMapping("/getProduct")	
    public ModelAndView getProduct(Long code) {		
        ModelAndView mav = new ModelAndView("productDetails");		
        System.out.println(code);		
        mav.addObject(repo.findById(code).get());		
        return mav;	} 
    }