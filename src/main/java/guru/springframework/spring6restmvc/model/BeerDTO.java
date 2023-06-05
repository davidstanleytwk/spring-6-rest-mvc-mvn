package guru.springframework.spring6restmvc.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class BeerDTO {
    public static final class VALIDATION_ERROR {
        public static final String NULL ="is required";
        public static final String BLANK ="must not be empty";

    }
    private UUID id;
    private Integer version;
    @NotNull(message = VALIDATION_ERROR.NULL)
    @NotBlank(message = VALIDATION_ERROR.BLANK)
    private String beerName;

    @NotNull(message = "is required")
    private BeerStyle beerStyle;
    @NotNull(message = "UPC is required")
    @NotBlank(message = "UPC can not be an empty or whitespace")
    private String upc;
    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;


}
