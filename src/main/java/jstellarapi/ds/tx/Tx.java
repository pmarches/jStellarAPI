package jstellarapi.ds.tx;

import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Tx {

	@Expose
	@Valid
	private String error;
	@Expose
	@Valid
	private String error_message;
	@Expose
	@Valid
	private long error_code;
	@Expose
	@Valid
	private Result result;

	/**
	 * 
	 * @return The result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * 
	 * @param result
	 *            The result
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	public Tx withResult(Result result) {
		this.result = result;
		return this;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public long getError_code() {
		return error_code;
	}

	public void setError_code(long error_code) {
		this.error_code = error_code;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(result).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Tx) == false) {
			return false;
		}
		Tx rhs = ((Tx) other);
		return new EqualsBuilder().append(result, rhs.result).isEquals();
	}

}
