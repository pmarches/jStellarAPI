
package jstellarapi.ds.tx;

import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class ModifiedNode {

    @Expose
    @Valid
    private jstellarapi.ds.tx.FinalFields FinalFields;
    @Expose
    private String LedgerEntryType;
    @Expose
    private String LedgerIndex;
    @Expose
    @Valid
    private jstellarapi.ds.tx.PreviousFields PreviousFields;
    @Expose
    private String PreviousTxnID;
    @Expose
    private long PreviousTxnLgrSeq;

    /**
     * 
     * @return
     *     The FinalFields
     */
    public jstellarapi.ds.tx.FinalFields getFinalFields() {
        return FinalFields;
    }

    /**
     * 
     * @param FinalFields
     *     The FinalFields
     */
    public void setFinalFields(jstellarapi.ds.tx.FinalFields FinalFields) {
        this.FinalFields = FinalFields;
    }

    public ModifiedNode withFinalFields(jstellarapi.ds.tx.FinalFields FinalFields) {
        this.FinalFields = FinalFields;
        return this;
    }

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

    public ModifiedNode withLedgerEntryType(String LedgerEntryType) {
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

    public ModifiedNode withLedgerIndex(String LedgerIndex) {
        this.LedgerIndex = LedgerIndex;
        return this;
    }

    /**
     * 
     * @return
     *     The PreviousFields
     */
    public jstellarapi.ds.tx.PreviousFields getPreviousFields() {
        return PreviousFields;
    }

    /**
     * 
     * @param PreviousFields
     *     The PreviousFields
     */
    public void setPreviousFields(jstellarapi.ds.tx.PreviousFields PreviousFields) {
        this.PreviousFields = PreviousFields;
    }

    public ModifiedNode withPreviousFields(jstellarapi.ds.tx.PreviousFields PreviousFields) {
        this.PreviousFields = PreviousFields;
        return this;
    }

    /**
     * 
     * @return
     *     The PreviousTxnID
     */
    public String getPreviousTxnID() {
        return PreviousTxnID;
    }

    /**
     * 
     * @param PreviousTxnID
     *     The PreviousTxnID
     */
    public void setPreviousTxnID(String PreviousTxnID) {
        this.PreviousTxnID = PreviousTxnID;
    }

    public ModifiedNode withPreviousTxnID(String PreviousTxnID) {
        this.PreviousTxnID = PreviousTxnID;
        return this;
    }

    /**
     * 
     * @return
     *     The PreviousTxnLgrSeq
     */
    public long getPreviousTxnLgrSeq() {
        return PreviousTxnLgrSeq;
    }

    /**
     * 
     * @param PreviousTxnLgrSeq
     *     The PreviousTxnLgrSeq
     */
    public void setPreviousTxnLgrSeq(long PreviousTxnLgrSeq) {
        this.PreviousTxnLgrSeq = PreviousTxnLgrSeq;
    }

    public ModifiedNode withPreviousTxnLgrSeq(long PreviousTxnLgrSeq) {
        this.PreviousTxnLgrSeq = PreviousTxnLgrSeq;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(FinalFields).append(LedgerEntryType).append(LedgerIndex).append(PreviousFields).append(PreviousTxnID).append(PreviousTxnLgrSeq).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifiedNode) == false) {
            return false;
        }
        ModifiedNode rhs = ((ModifiedNode) other);
        return new EqualsBuilder().append(FinalFields, rhs.FinalFields).append(LedgerEntryType, rhs.LedgerEntryType).append(LedgerIndex, rhs.LedgerIndex).append(PreviousFields, rhs.PreviousFields).append(PreviousTxnID, rhs.PreviousTxnID).append(PreviousTxnLgrSeq, rhs.PreviousTxnLgrSeq).isEquals();
    }

}
