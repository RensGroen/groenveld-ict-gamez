package nl.groenveldict.gamez.whoamiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewGameResponse {
    private String id;
    private List<PlayerDetailsResponse> playerDetails;
}
