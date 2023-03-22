package jp.co.axa.apidemo.model;


import lombok.*;

import java.io.Serializable;

/**
 * Class is responsible for employee details for the outside layer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeModel  implements Serializable {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Integer salary;

    @NonNull
    private String department;
}
