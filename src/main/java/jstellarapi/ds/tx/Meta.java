
package jstellarapi.ds.tx;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.validation.Valid;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Meta {

    @Expose
    @Valid
    private List<AffectedNode> AffectedNodes = new ArrayList<AffectedNode>();
    @Expose
    private long TransactionIndex;
    @Expose
    private String TransactionResult;

    /**
     * 
     * @return
     *     The AffectedNodes
     */
    public List<AffectedNode> getAffectedNodes() {
        return AffectedNodes;
    }

    /**
     * 
     * @param AffectedNodes
     *     The AffectedNodes
     */
    public void setAffectedNodes(List<AffectedNode> AffectedNodes) {
        this.AffectedNodes = AffectedNodes;
    }

    public Meta withAffectedNodes(List<AffectedNode> AffectedNodes) {
        this.AffectedNodes = AffectedNodes;
        return this;
    }

    /**
     * 
     * @return
     *     The TransactionIndex
     */
    public long getTransactionIndex() {
        return TransactionIndex;
    }

    /**
     * 
     * @param TransactionIndex
     *     The TransactionIndex
     */
    public void setTransactionIndex(long TransactionIndex) {
        this.TransactionIndex = TransactionIndex;
    }

    public Meta withTransactionIndex(long TransactionIndex) {
        this.TransactionIndex = TransactionIndex;
        return this;
    }

    /**
     * 
     * @return
     *     The TransactionResult
     */
    public String getTransactionResult() {
        return TransactionResult;
    }

    /**
     * 
     * @param TransactionResult
     *     The TransactionResult
     */
    public void setTransactionResult(String TransactionResult) {
        this.TransactionResult = TransactionResult;
    }

    public Meta withTransactionResult(String TransactionResult) {
        this.TransactionResult = TransactionResult;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(AffectedNodes).append(TransactionIndex).append(TransactionResult).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meta) == false) {
            return false;
        }
        Meta rhs = ((Meta) other);
        return new EqualsBuilder().append(AffectedNodes, rhs.AffectedNodes).append(TransactionIndex, rhs.TransactionIndex).append(TransactionResult, rhs.TransactionResult).isEquals();
    }

}
