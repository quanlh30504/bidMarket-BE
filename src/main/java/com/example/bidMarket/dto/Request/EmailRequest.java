package com.example.bidMarket.dto.Request;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    public String email;
}
