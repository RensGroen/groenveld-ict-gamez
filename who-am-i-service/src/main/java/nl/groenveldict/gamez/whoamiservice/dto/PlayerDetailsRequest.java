package nl.groenveldict.gamez.whoamiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDetailsRequest {
    private int userId;
    private int opponentId;
    private String person;
}
