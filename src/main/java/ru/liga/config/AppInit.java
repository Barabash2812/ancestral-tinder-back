package ru.liga.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.liga.database.entity.User;
import ru.liga.database.repository.UserRepository;
import ru.liga.enums.SexType;
import ru.liga.model.dto.ProfileDTO;
import ru.liga.payload.SignUpRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppInit implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {

        List<User> users = new ArrayList<>();

        addUser(users, "ilyat", "password", "ilya.tikhovskiy@mail.ru",
                new ProfileDTO("ilya", "28/14/14", SexType.MALE, "Something about me"));
        addUser(users, "didelibalta", "password", "di.delibalta@gmail.com",
                new ProfileDTO("diana", "18/14/14", SexType.FEMALE, "Something about me"));
        addUser(users, "supervesio", "password", "another1@gmail.com",
                new ProfileDTO("vesya", "16/15/15", SexType.FEMALE, "Something about me"));
        addUser(users, "mariya", "password", "another5@gmail.com",
                new ProfileDTO("mariya95", "16/15/15", SexType.FEMALE, "Something about me"));
        addUser(users, "mxmtrms", "password", "another2@gmail.com",
                new ProfileDTO("max", "14/15/15", SexType.MALE, "Something about me"));
        addUser(users, "danya", "password", "another3@gmail.com",
                new ProfileDTO("danya", "10/15/15", SexType.MALE, "Something about me"));

        userRepository.saveAll(users);

        addMatch(users, 1, 0);

        userRepository.saveAll(users);
    }

    private void addUser(List<User> users, String username, String password, String email, ProfileDTO profileDTO) {
        User user = new User(new SignUpRequest(username, password, email, profileDTO.getName(), profileDTO.getDateOfBirth(), profileDTO.getSex(), profileDTO.getAbout()));
        if (!userRepository.existsUserByUsername(username))
            users.add(user);
    }

    private void addMatch(List<User> users, Integer from, Integer to) {
        if (users.size() >= Math.max(from, to)) {
            users.get(from).getProfile().getLovers().add(users.get(to).getProfile());
            users.get(to).getProfile().getBelovers().add(users.get(from).getProfile());
        }
    }
}
