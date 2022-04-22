package myprojectmessenger.controllers;

import myprojectmessenger.dao.SearchDao;
import myprojectmessenger.entity.User;
import myprojectmessenger.model.UserSearch;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    private final SearchDao searchDao;

    public SearchController(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    @GetMapping("/api/search")
    public List<UserSearch> searchUser(@RequestParam String searchString) {
        List<UserSearch> userSearches = new ArrayList<>();
        for (User user : searchDao.searchUser(searchString)) {
            UserSearch userSearch = new UserSearch();
            userSearch.setId(user.getId());
            userSearch.setName(user.getName());
            userSearch.setLogin(user.getAccount().getLogin());
            userSearches.add(userSearch);
        }
        return userSearches;
    }
}
