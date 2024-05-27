package nl.groenveldict.gamez.whoamiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(value = "game")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Game {
    
    @Id
    private String id;
    private boolean ended;
    private int userIdLost;
    private List<Integer> userIdsPlaying;

    @CreatedDate
    private LocalDateTime dateCreated;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    @Version
    private Integer version;

    private List<PlayerDetails> playerDetails;

}
