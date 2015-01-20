package jstellarapi.core;

public class StellarEngineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9010916677189222829L;
	private final String engine_result, engine_result_message;
	private final long engine_result_code;

	public StellarEngineException(String engine_result, long engine_result_code, String engine_result_message) {
		super(engine_result_message);
		this.engine_result = engine_result;
		this.engine_result_code = engine_result_code;
		this.engine_result_message = engine_result_message;
	}

	public String getEngine_result() {
		return engine_result;
	}

	public long getEngine_result_code() {
		return engine_result_code;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StellarEngineException [engine_result=").append(engine_result).append(", engine_result_code=").append(engine_result_code).append(", engine_result_message=")
				.append(engine_result_message).append("]");
		return builder.toString();
	}

}
