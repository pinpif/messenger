package myprojectmessenger.controllers;

import myprojectmessenger.dao.AuthorizationDao;
import myprojectmessenger.dao.BlockDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.BlockUser;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
public class BlockController {
    private final AuthorizationDao authorizationDao;
    private final BlockDao blockDao;

    public BlockController(AuthorizationDao authorizationDao, BlockDao blockDao) {
        this.authorizationDao = authorizationDao;
        this.blockDao = blockDao;
    }

    @PostMapping("/api/block")
    public void userBlock(@RequestHeader(value = "SessionId") String sessionId,
                          @RequestBody BlockUser blockUser) {
        User user = authorizationDao.findUserBySessionId(sessionId);
        User block = authorizationDao.findUsersByIds(Collections.singletonList(blockUser.getBlockUserId())).get(0);
        blockDao.addBlock(user, block);
    }

    @DeleteMapping("/api/unblock")
    public void unblockUser(@RequestHeader(value = "SessionId") String sessionId,
                            @RequestBody BlockUser blockUser) {
        User user = authorizationDao.findUserBySessionId(sessionId);
        User block = authorizationDao.findUsersByIds(Collections.singletonList(blockUser.getBlockUserId())).get(0);
        blockDao.unblockUser(user, block);
    }
}
