package demo.login.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import demo.login.data.User;
import demo.login.payload.request.PassworedUpdateRequest;
import demo.login.payload.request.UserUpdateRequest;
import demo.login.payload.response.UserResponse;
import demo.login.repository.BlobStorageRepository;
import demo.login.repository.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonService commonService;

    @Autowired
    private BlobStorageRepository blobStorageRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/users/{id}")
    public UserResponse getUserData(@PathVariable(name = "id") Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        return commonService.mapUserToUserResponse(user);
    }

    @PutMapping("/userUpdate")
    public UserResponse updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) throws Exception {
        Long userId = userUpdateRequest.getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        if (userUpdateRequest.getBranch() != null && !userUpdateRequest.getBranch().isBlank()) {
            user.setBranch(userUpdateRequest.getBranch());
        }
        if (userUpdateRequest.getCourse() != null && !userUpdateRequest.getCourse().isBlank()) {
            user.setCourse(userUpdateRequest.getCourse());
        }
        if (userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().isBlank()) {
            user.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getPassoutYear() != null && !userUpdateRequest.getPassoutYear().isBlank()) {
            user.setPassoutYear(userUpdateRequest.getPassoutYear());
        }
        if (userUpdateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        }
        User updatedUser = userRepository.save(user);
        return commonService.mapUserToUserResponse(updatedUser);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody PassworedUpdateRequest passworedUpdateRequest)
            throws Exception {
        Long userId = passworedUpdateRequest.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        Boolean matched = encoder.matches(passworedUpdateRequest.getCurrentPassword(), user.getPassword());
        if (!matched) {
            throw new Exception("Incorrect Password");
        }
        user.setPassword(encoder.encode(passworedUpdateRequest.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/updatePhoto")
    public UserResponse updatePhoto(@RequestParam(name = "userId") Long userId,
            @RequestParam(name = "file") MultipartFile file) throws Exception {
        if (file != null && file.getContentType().startsWith("image")) {

            String fileUrl = blobStorageRepository.uploadFile("postdocuments", file);
            User user = userRepository.findById(userId).get();
            user.setFileurl(fileUrl);
            User updatedUser = userRepository.save(user);
            return commonService.mapUserToUserResponse(updatedUser);
        }
        throw new Exception("File should not be null and of type image");
    }

}
