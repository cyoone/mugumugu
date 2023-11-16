package com.example.mugu.controller;

import com.example.mugu.dto.CategoryDTO;
import com.example.mugu.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category") // 상위 주소값
public class CategoryController {
    // 생성자
    private final CategoryService categoryService;

    @GetMapping("/save")
    public String saveForm() {
        return "category/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CategoryDTO categoryDTO) {
        System.out.println("categoryDTO = " + categoryDTO);
        categoryService.save(categoryDTO);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<CategoryDTO> categoryDTOList = categoryService.findAll();
        model.addAttribute("categoryList", categoryDTOList);
        return "category/list";
    }

    @GetMapping("/{category_id}")
    public String findById(@PathVariable Long category_id, Model model) {
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
        */
        CategoryDTO categoryDTO = categoryService.findById(category_id);  // 해당 게시글을 가져와 DTO로 받음
        model.addAttribute("category", categoryDTO);  // 받은 DTO를 모델로 가져와 detail로 보냄
        return "category/detail";
    }

    @GetMapping("/update/{category_id}")
    public String updateForm(@PathVariable Long category_id, Model model) {
        CategoryDTO categoryDTO = categoryService.findById(category_id);
        model.addAttribute("categoryUpdate", categoryDTO);
        return "category/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CategoryDTO categoryDTO, Model model) {
        CategoryDTO category = categoryService.update(categoryDTO);
        model.addAttribute("category", category);
        return "category/detail";
//        return "redirect:/board/" + boardDTO.getId(); => 수정을 했는데 조회수가 올라가는 문제가 생길 수 있음
    }

    @GetMapping("/delete/{category_id}")
    public String delete(@PathVariable Long category_id) {
        categoryService.delete(category_id);
        return "redirect:/category/";
    }

}
