package lxk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private long id;


    public Book(long id) {
        this.id = id;
    }


    /**
     * 书名
     */
    @NotEmpty(message = "书名不能为空")
    private String bookName;

    /**
     * ISBN号
     */
    @NotNull(message = "ISBN号不能为空")
    private String bookIsbn;

    /**
     * 单价
     */
    @DecimalMin(value = "0.1", message = "单价最低为0.1")
   // @Pattern(regexp = "^[0-9]+([.]{1}[0-9]+){0,1}$", message = "单价最低为0.1")//@Pattern 只对字符串有用
    private double price;

    /**
     * 单价
     */
    //@DecimalMin(value = "0.1", message = "单价最低为0.1")
    @Pattern(regexp = "^[0-9]+([.]{1}[0-9]+){0,1}$", message = "String单价最低为0.1")//@Pattern 只对字符串有用
    @NotEmpty(message = "单价不能为空")
    private String thePrice;
}
