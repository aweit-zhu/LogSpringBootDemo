package com.example.demo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.annotation.LogAction;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@GetMapping
	@LogAction(name = "使用者查詢產品")
	public String find() {
		return "使用者查詢產品";
	}
	
	@PostMapping()
	@LogAction(name = "使用者新增產品")
	public String create() {
		return "使用者新增產品";
	}
	
	@PutMapping()
	@LogAction(name = "使用者更新產品")
	public String update() {
		return "使用者更新產品";
	}
	
	@DeleteMapping()
	@LogAction(name = "使用者刪除產品")
	public String delete() {
		return "使用者刪除產品";
	}
}
