package com.newtonbox;

import com.newtonbox.Models.Permission;
import com.newtonbox.Models.Role;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.Repository.IUserRepository;
import com.newtonbox.utils.PermissionEnum;
import com.newtonbox.utils.RoleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class NewtonboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewtonboxApplication.class, args);
	}



}
