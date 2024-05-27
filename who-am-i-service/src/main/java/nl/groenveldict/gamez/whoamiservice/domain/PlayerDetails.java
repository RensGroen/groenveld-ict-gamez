package nl.groenveldict.gamez.whoamiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDetails {
    private int userId;
    private int opponentId;
    private String person;
}