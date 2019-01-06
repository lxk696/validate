package lxk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
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
    @NotBlank(message = "书名不能为空")
    @Pattern(regexp = "[a-zA-Z]{1,6}", message = "bookName长度需为1到6个字符之间的字母", groups = {Second.class}) //@Pattern 只对字符串有用
    @Length(min = 5, max = 20, message = "bookName长度必须在5到20之间", groups = {First.class})
    private String bookName;

    /**
     * ISBN号
     */
    @NotBlank(message = "ISBN号不能为空")
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
