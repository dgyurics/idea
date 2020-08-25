package idea.model.dto;

import idea.model.Category;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequestModel {
  private Category category;
  private Integer priority;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  @NotBlank
  private String imageUri;
  private String imageAlt;
  private String imageStyle;
}
