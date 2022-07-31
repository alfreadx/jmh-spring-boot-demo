package com.github.alfreadx.jmhspringdemo.service;

import com.github.alfreadx.jmhspringdemo.model.User;
import com.github.alfreadx.jmhspringdemo.model.UserCreateIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

//    private final UserRepository userRepo;
//
//    @Autowired
//    public UserService(UserRepository userRepo) {
//        this.userRepo = userRepo;
//    }


    private static Map<Long, User> DATASET = new HashMap<>() {{
        put(1L, User.builder()
                .id(1L)
                .account("afu001")
                .password("haha")
                .createTime(Timestamp.valueOf("2021-10-23 12:37:02"))
                .updateTime(Timestamp.valueOf("2021-10-23 12:37:02"))
                .build());
        put(2L, User.builder()
                .id(2L)
                .account("afu002")
                .password("haha")
                .createTime(Timestamp.valueOf("2021-10-30 12:37:02"))
                .updateTime(Timestamp.valueOf("2021-10-30 12:37:02"))
                .build());
        put(3L, User.builder()
                .id(3L)
                .account("afu003")
                .password("haha")
                .createTime(Timestamp.valueOf("2021-11-02 12:37:02"))
                .updateTime(Timestamp.valueOf("2021-11-02 12:37:02"))
                .build());

    }};

    private static Map<Long, Map<Long, User>> MAIN_TABLE = new HashMap<>() {{
        put(0L, DATASET);
    }};


    private AtomicLong MAX_ID = new AtomicLong(3);


    public Optional<User> findUser(Long id) {
        User user = getData(id);
        return Optional.ofNullable(user);
    }

    public void createUser(UserCreateIO createIO) {
        User user = new User();
        BeanUtils.copyProperties(createIO, user);

        add(user);
    }

    public void createUser2(UserCreateIO createIO) {
        User user = User.builder()
                .account(createIO.getAccount())
                .password(createIO.getPassword())
                .build();
        add(user);
    }

    private User add(User user) {
        long id = MAX_ID.incrementAndGet();
        user.setId(id);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setCreateTime(now);
        user.setUpdateTime(now);

        saveData(user);
        return user;
    }

    private Long getHash(Long userId) {
        return userId / 1000;
    }

    private void saveData(User user) {
        Long hash = getHash(user.getId());
        Map<Long, User> map = MAIN_TABLE.getOrDefault(hash, new HashMap<>());
        map.put(user.getId(), user);
        MAIN_TABLE.put(hash, map);
    }

    private User getData(Long id) {
        Map<Long, User> map = MAIN_TABLE.getOrDefault(getHash(id), Collections.emptyMap());
        return map.get(id);
    }

    public List<User> getUsers() {
        return MAIN_TABLE.values().stream()
                .flatMap(e -> e.values().stream())
                .collect(Collectors.toList());
    }
}
