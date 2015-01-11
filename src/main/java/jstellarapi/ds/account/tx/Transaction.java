
package jstellarapi.ds.account.tx;

import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Transaction {

    @Expose
    @Valid
    private Meta meta;
    @Expose
    @Valid
    private Tx tx;
    @Expose
    private boolean validated;

    /**
     * 
     * @return
     *     The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * 
     * @param meta
     *     The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Transaction withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    /**
     * 
     * @return
     *     The tx
     */
    public Tx getTx() {
        return tx;
    }

    /**
     * 
     * @param tx
     *     The tx
     */
    public void setTx(Tx tx) {
        this.tx = tx;
    }

    public Transaction withTx(Tx tx) {
        this.tx = tx;
        return this;
    }

    /**
     * 
     * @return
     *     The validated
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * 
     * @param validated
     *     The validated
     */
    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public Transaction withValidated(boolean validated) {
        this.validated = validated;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(meta).append(tx).append(validated).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Transaction) == false) {
            return false;
        }
        Transaction rhs = ((Transaction) other);
        return new EqualsBuilder().append(meta, rhs.meta).append(tx, rhs.tx).append(validated, rhs.validated).isEquals();
    }

}
