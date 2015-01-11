
package jstellarapi.ds.account.tx;

import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class CreatedNode {

    @Expose
    private String LedgerEntryType;
    @Expose
    private String LedgerIndex;
    @Expose
    @Valid
    private jstellarapi.ds.account.tx.NewFields NewFields;

    /**
     * 
     * @return
     *     The LedgerEntryType
     */
    public String getLedgerEntryType() {
        return LedgerEntryType;
    }

    /**
     * 
     * @param LedgerEntryType
     *     The LedgerEntryType
     */
    public void setLedgerEntryType(String LedgerEntryType) {
        this.LedgerEntryType = LedgerEntryType;
    }

    public CreatedNode withLedgerEntryType(String LedgerEntryType) {
        this.LedgerEntryType = LedgerEntryType;
        return this;
    }

    /**
     * 
     * @return
     *     The LedgerIndex
     */
    public String getLedgerIndex() {
        return LedgerIndex;
    }

    /**
     * 
     * @param LedgerIndex
     *     The LedgerIndex
     */
    public void setLedgerIndex(String LedgerIndex) {
        this.LedgerIndex = LedgerIndex;
    }

    public CreatedNode withLedgerIndex(String LedgerIndex) {
        this.LedgerIndex = LedgerIndex;
        return this;
    }

    /**
     * 
     * @return
     *     The NewFields
     */
    public jstellarapi.ds.account.tx.NewFields getNewFields() {
        return NewFields;
    }

    /**
     * 
     * @param NewFields
     *     The NewFields
     */
    public void setNewFields(jstellarapi.ds.account.tx.NewFields NewFields) {
        this.NewFields = NewFields;
    }

    public CreatedNode withNewFields(jstellarapi.ds.account.tx.NewFields NewFields) {
        this.NewFields = NewFields;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(LedgerEntryType).append(LedgerIndex).append(NewFields).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreatedNode) == false) {
            return false;
        }
        CreatedNode rhs = ((CreatedNode) other);
        return new EqualsBuilder().append(LedgerEntryType, rhs.LedgerEntryType).append(LedgerIndex, rhs.LedgerIndex).append(NewFields, rhs.NewFields).isEquals();
    }

}
