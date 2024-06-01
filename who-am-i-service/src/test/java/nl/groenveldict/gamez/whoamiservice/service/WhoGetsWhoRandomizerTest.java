package nl.groenveldict.gamez.whoamiservice.service;

import nl.groenveldict.gamez.whoamiservice.dto.PlayerDetailsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * This test class is non-deterministic, because the method under test
 * will return random results, which is exactly the purpose of that method.
 */
public class WhoGetsWhoRandomizerTest {

    private List<PlayerDetailsResponse> playerDetailsResponses;

    @BeforeEach
    public void before(){
        this.playerDetailsResponses = WhoGetsWhoRandomizer
                .randomizeWhoGetsWho(Arrays.asList(1, 2, 3, 4, 5, 8, 10));
    }

    @Test
    public void randomizeWhoGetsWhoMustHaveUniqueUserIds(){
        Assertions.assertEquals(this.playerDetailsResponses.size(), this.playerDetailsResponses.stream()
                .map(PlayerDetailsResponse::getUserId)
                .distinct()
                .count());
    }

    @Test
    public void randomizeWhoGetsWhoMustHaveUniqueUOpponentIds(){
        Assertions.assertEquals(this.playerDetailsResponses.size(), this.playerDetailsResponses.stream()
                .map(PlayerDetailsResponse::getOpponentId)
                .distinct()
                .count());
    }

    @Test
    public void randomizeWhoGetsWhoMustNotGetSameUserIdAsOpponentIdPerObject(){
        for (PlayerDetailsResponse pdr : this.playerDetailsResponses){
            Assertions.assertNotEquals(pdr.getUserId(), pdr.getOpponentId());
        }
    }

    @Test
    public void randomizeWhoGetsWhoMustSortBasedOnUserId(){
        for (int index = 1; index < this.playerDetailsResponses.size(); index++){
            Assertions.assertTrue(this.playerDetailsResponses.get(index).getUserId() > this.playerDetailsResponses.get(index -1).getUserId());
        }
    }
}
