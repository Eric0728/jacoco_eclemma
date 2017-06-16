public  final class  ErrorResponseBuilder {

	private HttpServletRequest request;
	
	private String message;

	private String exception;
	
	private final HttpStatus status;
	
	private String messageKey = "backend.fatal.unknown";
	private String category = "FATAL";


	//
	private ErrorResponseBuilder(HttpStatus status) {
		this.status = status;
	}

	public static ErrorResponseBuilder create(HttpStatus status) {
		return new ErrorResponseBuilder(status);
	}
	
	public static ErrorResponseBuilder badRequest() {
		return ErrorResponseBuilder.create(HttpStatus.BAD_REQUEST);
	}

	public static ErrorResponseBuilder internalServerError() {
		return ErrorResponseBuilder.create(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public static ErrorResponseBuilder unauthorized() {
		return ErrorResponseBuilder.create(HttpStatus.UNAUTHORIZED);
	}

	public static ErrorResponseBuilder forbidden() {
		return ErrorResponseBuilder.create(HttpStatus.FORBIDDEN);
	}
	//
	
	public ErrorResponseBuilder withException(Exception exception) {
		this.message = exception.getMessage();
		this.exception = Optional.<Class<?>> ofNullable( exception.getClass().getEnclosingClass() )
				.orElseGet(exception::getClass).getSimpleName();
		return this;
	}
	
	public ErrorResponseBuilder withRequest(HttpServletRequest request) {
		this.request = request;
		return this;
	}
	
	public Map<String, Object> asMap() {
		final Map<String, Object> body = 
				new Fluent.HashMap<String, Object>()
				.append("timestamp", Instant.now())
				.append("status",   status.value())
				.append("category", category)
				.append("messageKey", messageKey)
				.append("error", status.getReasonPhrase())
				.append("exception", exception)
				.append("message", message)
				.append("path", Optional.ofNullable(request).map(r -> request.getRequestURL()).orElse(null));

		return Collections.unmodifiableMap(body);
	}
	
	/**
	 * 
	 * @return ResponseEntity
	 */
	public ResponseEntity< Map<String, Object> > build() {
		return new ResponseEntity<>(asMap(), status);
	}

}
