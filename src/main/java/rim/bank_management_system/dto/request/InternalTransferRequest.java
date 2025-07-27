package rim.bank_management_system.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InternalTransferRequest extends TransferRequest {
    private Long toAccountId;
}
