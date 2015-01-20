
package jstellarapi.ds.tx;

import javax.annotation.Generated;
import javax.validation.Valid;

import jstellarapi.core.DenominatedIssuedCurrency;
import jstellarapi.core.StellarAddress;
import jstellarapi.core.StellarPaymentTransaction;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Result {

    @Expose
    private String Account;
    @Expose
    @Valid
    private Amount amount;
    @Expose
    private String Destination;
    @Expose
    private String Fee;
    @Expose
    private long Flags;
    @Expose
    private long sequence;
    @Expose
    private String SigningPubKey;
    @Expose
    private String TransactionType;
    @Expose
    private String TxnSignature;
    @Expose
    private String hash;
    @Expose
    private long inLedger;
    @SerializedName("ledger_index")
    @Expose
    private long ledgerIndex;
    @Expose
    @Valid
    private Meta meta;
    @Expose
    private String status;
    @Expose
    private boolean validated;

    /**
     * 
     * @return
     *     The Account
     */
    public String getAccount() {
        return Account;
    }

    /**
     * 
     * @param Account
     *     The Account
     */
    public void setAccount(String Account) {
        this.Account = Account;
    }

    public Result withAccount(String Account) {
        this.Account = Account;
        return this;
    }

    /**
     * 
     * @return
     *     The Amount
     */
    public Amount getAmount() {
        return amount;
    }

    /**
     * 
     * @param Amount
     *     The Amount
     */
    public void setAmount(Amount Amount) {
        this.amount = Amount;
    }

    public Result withAmount(Amount Amount) {
        this.amount = Amount;
        return this;
    }

    /**
     * 
     * @return
     *     The Destination
     */
    public String getDestination() {
        return Destination;
    }

    /**
     * 
     * @param Destination
     *     The Destination
     */
    public void setDestination(String Destination) {
        this.Destination = Destination;
    }

    public Result withDestination(String Destination) {
        this.Destination = Destination;
        return this;
    }

    /**
     * 
     * @return
     *     The Fee
     */
    public String getFee() {
        return Fee;
    }

    /**
     * 
     * @param Fee
     *     The Fee
     */
    public void setFee(String Fee) {
        this.Fee = Fee;
    }

    public Result withFee(String Fee) {
        this.Fee = Fee;
        return this;
    }

    /**
     * 
     * @return
     *     The Flags
     */
    public long getFlags() {
        return Flags;
    }

    /**
     * 
     * @param Flags
     *     The Flags
     */
    public void setFlags(long Flags) {
        this.Flags = Flags;
    }

    public Result withFlags(long Flags) {
        this.Flags = Flags;
        return this;
    }

    /**
     * 
     * @return
     *     The Sequence
     */
    public long getSequence() {
        return sequence;
    }

    /**
     * 
     * @param Sequence
     *     The Sequence
     */
    public void setSequence(long Sequence) {
        this.sequence = Sequence;
    }

    public Result withSequence(long Sequence) {
        this.sequence = Sequence;
        return this;
    }

    /**
     * 
     * @return
     *     The SigningPubKey
     */
    public String getSigningPubKey() {
        return SigningPubKey;
    }

    /**
     * 
     * @param SigningPubKey
     *     The SigningPubKey
     */
    public void setSigningPubKey(String SigningPubKey) {
        this.SigningPubKey = SigningPubKey;
    }

    public Result withSigningPubKey(String SigningPubKey) {
        this.SigningPubKey = SigningPubKey;
        return this;
    }

    /**
     * 
     * @return
     *     The TransactionType
     */
    public String getTransactionType() {
        return TransactionType;
    }

    /**
     * 
     * @param TransactionType
     *     The TransactionType
     */
    public void setTransactionType(String TransactionType) {
        this.TransactionType = TransactionType;
    }

    public Result withTransactionType(String TransactionType) {
        this.TransactionType = TransactionType;
        return this;
    }

    /**
     * 
     * @return
     *     The TxnSignature
     */
    public String getTxnSignature() {
        return TxnSignature;
    }

    /**
     * 
     * @param TxnSignature
     *     The TxnSignature
     */
    public void setTxnSignature(String TxnSignature) {
        this.TxnSignature = TxnSignature;
    }

    public Result withTxnSignature(String TxnSignature) {
        this.TxnSignature = TxnSignature;
        return this;
    }

    /**
     * 
     * @return
     *     The hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * 
     * @param hash
     *     The hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    public Result withHash(String hash) {
        this.hash = hash;
        return this;
    }

    /**
     * 
     * @return
     *     The inLedger
     */
    public long getInLedger() {
        return inLedger;
    }

    /**
     * 
     * @param inLedger
     *     The inLedger
     */
    public void setInLedger(long inLedger) {
        this.inLedger = inLedger;
    }

    public Result withInLedger(long inLedger) {
        this.inLedger = inLedger;
        return this;
    }

    /**
     * 
     * @return
     *     The ledgerIndex
     */
    public long getLedgerIndex() {
        return ledgerIndex;
    }

    /**
     * 
     * @param ledgerIndex
     *     The ledger_index
     */
    public void setLedgerIndex(long ledgerIndex) {
        this.ledgerIndex = ledgerIndex;
    }

    public Result withLedgerIndex(long ledgerIndex) {
        this.ledgerIndex = ledgerIndex;
        return this;
    }

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

    public Result withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Result withStatus(String status) {
        this.status = status;
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

    public Result withValidated(boolean validated) {
        this.validated = validated;
        return this;
    }
    
    public StellarPaymentTransaction toStellarPaymentTransaction(){
    	DenominatedIssuedCurrency dic;
    	if(amount.getIssuer()==null){
    		dic = new DenominatedIssuedCurrency(amount.getValue().toBigInteger());
    	}else{
    		dic = new DenominatedIssuedCurrency(amount.getValue(),new StellarAddress(amount.getIssuer()),amount.getCurrency());
    	}
    	StellarPaymentTransaction sptx = new StellarPaymentTransaction(new StellarAddress(Account), new StellarAddress(Destination), dic, (int) sequence);
    	sptx.flags=Flags;
    	sptx.publicKeyUsedToSign = SigningPubKey;
    	sptx.signature = TxnSignature;
    	sptx.txHash = hash;
    	return sptx;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(Account).append(amount).append(Destination).append(Fee).append(Flags).append(sequence).append(SigningPubKey).append(TransactionType).append(TxnSignature).append(hash).append(inLedger).append(ledgerIndex).append(meta).append(status).append(validated).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Result) == false) {
            return false;
        }
        Result rhs = ((Result) other);
        return new EqualsBuilder().append(Account, rhs.Account).append(amount, rhs.amount).append(Destination, rhs.Destination).append(Fee, rhs.Fee).append(Flags, rhs.Flags).append(sequence, rhs.sequence).append(SigningPubKey, rhs.SigningPubKey).append(TransactionType, rhs.TransactionType).append(TxnSignature, rhs.TxnSignature).append(hash, rhs.hash).append(inLedger, rhs.inLedger).append(ledgerIndex, rhs.ledgerIndex).append(meta, rhs.meta).append(status, rhs.status).append(validated, rhs.validated).isEquals();
    }

}
