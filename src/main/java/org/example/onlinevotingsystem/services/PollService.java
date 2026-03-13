package org.example.onlinevotingsystem.services;

import org.example.onlinevotingsystem.models.*;
import org.example.onlinevotingsystem.repositories.OptionRepository;
import org.example.onlinevotingsystem.repositories.PollRepository;
import org.example.onlinevotingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private UserService adminService;

    @Autowired
    private UserRepository voterRepository;

    @Autowired
    private NotificationService notificationService;



    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public void createPollWithOptions(PollRequest poll, List<String> optionTitles, List<String> optionWeights,
                                      String type) {

        Optional<User> adminUser = adminService.getVoterByUsername(Constants.ADMIN_TYPE_1_USER_NAME);

        if (adminUser.isPresent()) {
            poll.setAdmin(adminUser.get());
        }
        if (poll.getVotingStrategy().equals(Constants.WEIGHTED_METHOD) && optionWeights.size() == optionTitles.size()) {
            String weights = String.join("-", optionWeights);
            poll.setWeight(weights);
        }


    }

    public void castVote(int optionId, String username) {
        // Fetch the Option
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        // Increment vote count for the selected option
        option.setVoteCount(option.getVoteCount() + 1);
        optionRepository.save(option); // Persist updated option immediately

        // Retrieve the associated Poll
        Poll poll = option.getPoll();
        poll.setTotalVote(poll.getTotalVote() + 1);


        // Persist the poll
        pollRepository.save(poll);



        // Link the voter to the poll
        Optional<User> voter = voterRepository.findByUsername(username);
        voter.ifPresent(user -> {
            if (user.getVotedPolls() == null) {
                user.setVotedPolls(new ArrayList<>());
            }
            user.getVotedPolls().add(poll);
            poll.getVoters().add(user);
            voterRepository.save(user);
        });
}


    public Map<Integer, Boolean> getAlreadyVottedMap(User user) {
        Map<Integer, Boolean> map = new HashMap<>();
        user.getVotedPolls().forEach(poll -> map.put(poll.getPollId(), true));

        return map;
    }

    public Map<Integer, Boolean> getVotedOptions(List<Poll> polls, Long userId) {
        Map<Integer, Boolean> votedOptions = new HashMap<>();

        // Early return if inputs are null
        if (polls == null || userId == null) {
            return votedOptions;
        }




        return votedOptions;
    }



    public boolean toggleFavorite(Long pollId, String username) {
        Optional<Poll> pollOpt = pollRepository.findByPollId(pollId);

        if (pollOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        Poll poll = pollOpt.get();

        Optional<User> userOpt = voterRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = userOpt.get();

        if (user.getFavoritePolls().contains(poll)) {
            user.getFavoritePolls().remove(poll);
        } else {
            user.getFavoritePolls().add(poll);

        }

        voterRepository.save(user);
        return user.getFavoritePolls().contains(poll);
    }

    public Map<Integer, Boolean> getFavoritePolls(Long id) {
        Map<Integer, Boolean> favoritePolls = new HashMap<>();
        Optional<User> user = voterRepository.findById(id);
        if (user.isPresent()) {
            user.get().getFavoritePolls().forEach(poll -> favoritePolls.put(poll.getPollId(), true));
        }
        return favoritePolls;
    }

    public List<Poll> getFavoritePollsList(Long id) {

        Optional<User> user = voterRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getFavoritePolls();
        }
        return new ArrayList<>();
    }

}
