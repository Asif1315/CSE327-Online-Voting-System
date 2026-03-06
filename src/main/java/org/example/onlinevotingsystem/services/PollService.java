package org.example.onlinevotingsystem.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.onlinevotingsystem.models.Constants;
import org.example.onlinevotingsystem.models.Option;
import org.example.onlinevotingsystem.models.Poll;
import org.example.onlinevotingsystem.models.User;
import org.example.onlinevotingsystem.repositories.OptionRepository;
import org.example.onlinevotingsystem.repositories.PollRepository;
import org.example.onlinevotingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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


    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
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

        // First initialize all options to false
        for (Poll poll : polls) {
            if (poll.getOptions() != null) {
                for (Option option : poll.getOptions()) {
                    votedOptions.put((int) option.getOptionId(), false);
                }
            }
        }

        // Then mark the options the user has voted on as true
        for (Poll poll : polls) {
            if (poll.getOptions() != null) {
                for (Option option : poll.getOptions()) {
                    if (option.getUsers() != null) {
                        for (User user : option.getUsers()) {
                            if (userId.equals(user.getId())) {
                                votedOptions.put((int) option.getOptionId(), true);
                                break; // No need to check other users once we found a match
                            }
                        }
                    }
                }
            }
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
