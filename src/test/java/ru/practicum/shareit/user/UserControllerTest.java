package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private final List<UserDto> listUserDto = new ArrayList<>();

    private static final String X_HEADER = "X-Sharer-User-Id";

    UserDto userDto = UserDto
            .builder()
            .id(1L)
            .email("example@example.com")
            .name("name")
            .build();

    @Test
    public void testFindAll() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(userDto));

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(listUserDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                    .andExpect(jsonPath("$.[0].email", is(userDto.getEmail()), String.class))
                    .andExpect(jsonPath("$.[0].name", is(userDto.getName()), String.class));
    }

    @Test
    public void testFindUserById() throws Exception {
        Mockito.when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(userDto);

        mvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsBytes(userDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                    .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class))
                    .andExpect(jsonPath("$.name", is(userDto.getName()), String.class));
    }

    @Test
    public void testCreateUser() throws Exception {
        Mockito.when(userService.addUser(Mockito.any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(userDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                    .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class))
                    .andExpect(jsonPath("$.name", is(userDto.getName()), String.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyLong()))
                .thenReturn(userDto);

        mvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_HEADER, 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(mapper.writeValueAsString(userDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                    .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class))
                    .andExpect(jsonPath("$.name", is(userDto.getName()), String.class));
    }

    @Test
    public void testRemoveUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .header(X_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }
}
