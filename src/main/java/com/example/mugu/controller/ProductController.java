package com.example.mugu.controller;

import com.example.mugu.dto.CategoryDTO;
import com.example.mugu.dto.ProductDTO;
import com.example.mugu.service.CategoryService;
import com.example.mugu.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/save")
    public String saveForm(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "product/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ProductDTO productDTO) {
        System.out.println("productDTO = " + productDTO);
        productService.save(productDTO);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<ProductDTO> productDTOList = productService.findAll();
        model.addAttribute("productList", productDTOList);
        return "product/list";
    }

    @GetMapping("/{product_id}")
    public String findById(@PathVariable Long product_id, Model model) {
        // 게시글 조회수 하나 올리기
        productService.updateViews(product_id);
        ProductDTO productDTO = productService.findById(product_id);  // 해당 게시글을 가져와 DTO로 받음
        model.addAttribute("product", productDTO);  // 받은 DTO를 모델로 가져와 detail로 보냄
        return "product/detail";
    }

//    @GetMapping("/update/{product_id}")
//    public String updateForm(@PathVariable Long product_id, Model model) {
//        ProductDTO productDTO = productService.findById(product_id);
//        model.addAttribute("product", productDTO);
//        model.addAttribute("categories", categoryService.findAll());
//        return "product/update";
//    }
//
//    @PostMapping("/update")
//    public String update(@ModelAttribute ProductDTO productDTO, Model model) {
//        ProductDTO product = productService.update(productDTO);
//        model.addAttribute("product", product);
//        return "product/detail";
////        return "redirect:/board/" + boardDTO.getId(); => 수정을 했는데 조회수가 올라가는 문제가 생길 수 있음
//    }

    @GetMapping("/update/{product_id}")
    public String updateForm(@PathVariable Long product_id, Model model) {
        ProductDTO productDTO = productService.findById(product_id);
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categoryService.findAll());
        return "product/update";
    }

    @PostMapping("/update/{product_id}")
    public String update(@PathVariable Long product_id, @ModelAttribute ProductDTO productDTO) {
        productService.update(product_id, productDTO);
        return "product/detail";
    }

    @GetMapping("/delete/{product_id}")
    public String delete(@PathVariable Long product_id) {
        productService.delete(product_id);
        return "redirect:/product/";
    }

}
