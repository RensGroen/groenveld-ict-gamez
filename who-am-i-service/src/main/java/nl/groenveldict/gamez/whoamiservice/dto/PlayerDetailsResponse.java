package nl.groenveldict.gamez.whoamiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDetailsResponse {
    private int userId;
    private int opponentId;
}
