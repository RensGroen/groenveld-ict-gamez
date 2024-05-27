package nl.groenveldict.gamez.whoamiservice.service;

import nl.groenveldict.gamez.whoamiservice.dto.NewGameResponse;
import nl.groenveldict.gamez.whoamiservice.dto.PlayerDetailsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WhoGetsWhoRandomizer {

    static List<PlayerDetailsResponse> randomizeWhoGetsWho(List<Integer> userIdsPlaying) {
        List<PlayerDetailsResponse> playerDetails = new ArrayList<>();
        Collections.shuffle(userIdsPlaying);
        
        for (int index = 0; index < userIdsPlaying.size(); index++){
            playerDetails.add(PlayerDetailsResponse.builder()
                                                .userId(userIdsPlaying.get(index))
                                                .opponentId(determineOpponentId(userIdsPlaying, index))
                                                .build());

        }

        return playerDetails.stream()
                    .sorted((Comparator.comparingInt(PlayerDetailsResponse::getUserId)))
                    .toList();
    }

    private static Integer determineOpponentId(List<Integer> userIdsPlaying, int index) {
        return isLastIteration(userIdsPlaying, index) ? userIdsPlaying.getFirst() : getIdForNextIteration(userIdsPlaying, index);
    }

    private static boolean isLastIteration(List<Integer> userIdsPlaying, int index) {
        return index + 1 == userIdsPlaying.size();
    }

    private static Integer getIdForNextIteration(List<Integer> userIdsPlaying, int index) {
        return userIdsPlaying.get(index + 1);
    }
}
