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
