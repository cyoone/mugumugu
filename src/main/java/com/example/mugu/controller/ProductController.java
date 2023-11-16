package com.example.mugu.controller;

import com.example.mugu.dto.ProductDTO;
import com.example.mugu.entity.ProductFileEntity;
import com.example.mugu.service.CategoryService;
import com.example.mugu.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/save")
    public String saveForm(Model model, @ModelAttribute("productDTO") ProductDTO productDTO) {
        model.addAttribute("categories", categoryService.findValidCategories());
        return "product/save";
    }


    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("productDTO") ProductDTO productDTO, BindingResult result, Model model) {
        // 양식 유효성 검사
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findValidCategories());
            return "product/save"; // 상품 등록 페이지로 리턴
        }

        // 파일 개수 검사
        if (productDTO.getFiles().size() > 10) {
            result.rejectValue("files", "error.files", "사진 최대 갯수는 10개 입니다.");
            model.addAttribute("categories", categoryService.findValidCategories());
            return "product/save"; // 상품 등록 페이지로 리턴
        }

        productService.save(productDTO);
        return "index";
    }

    @GetMapping("/{product_id}")
    public String findById(@PathVariable Long product_id, Model model) {
        // 게시글 조회수 하나 올리기
        productService.updateViews(product_id);
        ProductDTO productDTO = productService.findById(product_id);  // 해당 게시글을 가져와 DTO로 받음
        model.addAttribute("product", productDTO);  // 받은 DTO를 모델로 가져와 detail로 보냄
        return "product/detail";
    }

    @GetMapping("/update/{product_id}")
    public String updateForm(@PathVariable Long product_id, Model model) {
        ProductDTO productDTO = productService.findById(product_id);
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categoryService.findValidCategories());

        return "product/update";
    }

    @PostMapping("/update/{product_id}")
    public String update(@PathVariable Long product_id, @Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult result, Model model,
                         @RequestParam Long category_id, @RequestParam(required = false) List<MultipartFile> newImages, @RequestParam(required = false) List<String> deleteImages) {
        // 유효성 검사
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "product/update";
        }
        // 이미지 삭제
        if (deleteImages != null) {
            for (String url : deleteImages) {
                productService.deleteImageByUrl(url);
            }
        }
        // 새로운 이미지 추가
        productService.addNewImages(product_id, newImages);

        // 카테고리 아이디 설정
        productDTO.setCategory_id(category_id);

        productService.update(product_id, productDTO);
        // 수정된 상품 정보를 가져옴
        ProductDTO updatedProductDTO = productService.findById(product_id);
        // 가져온 상품 정보를 모델에 추가
        model.addAttribute("product", updatedProductDTO);
        return "product/detail";
    }

    // 이미지 삭제
//    @PostMapping("/deleteImage")
//    public String deleteImage(@RequestParam String url, @RequestParam Long product_id) {
//        productService.deleteImageByUrl(url);
//        return "redirect:/product/update/" + product_id;
//    }

    @GetMapping("/delete/{product_id}")
    public String delete(@PathVariable Long product_id) {
        productService.delete(product_id);
        return "redirect:/product";
    }

    @GetMapping("/hide/{product_id}")
    public String hide(@PathVariable Long product_id) {
        productService.hide(product_id);
        return "redirect:/product/paging";
    }

    @GetMapping("/unhide/{product_id}")
    public String unhide(@PathVariable Long product_id) {
        productService.unhide(product_id);
        return "redirect:/product/paging";
    }

    @GetMapping("")
    public String paging(@PageableDefault(page = 1) Pageable pageable,
                         @RequestParam(required = false) String category,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false, defaultValue = "createdAt") String sort,
                         Model model) {

        // 카테고리 목록을 가져와 Model에 추가
        model.addAttribute("categories", categoryService.findValidCategories());

        pageable.getPageNumber();   // 요청된 페이지
        Page<ProductDTO> productList = productService.paging(pageable, category, name, sort);
        int blockLimit = 3;         // 한번에 나열되는 페이지 번호 갯수
        // 블록 시작 페이지 설정
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        // 블록 끝 페이지 설정
        int endPage = ((startPage + blockLimit - 1) < productList.getTotalPages()) ? startPage + blockLimit - 1 : productList.getTotalPages();

        model.addAttribute("category", category);
        model.addAttribute("name", name);
        model.addAttribute("sort", sort);
        model.addAttribute("productList", productList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "product/list";
    }

    // 확인용
    @GetMapping("/paging")
    public String findAll(Model model) {
        List<ProductDTO> productDTOList = productService.findAll();
        model.addAttribute("productList", productDTOList);
        return "product/listAll";
    }

    // ***********************************************프론트 확인용******************************************************
    @GetMapping("/main")
    public String main(Model model) {
        return "main";
    }

    @GetMapping("/list")
    public String list(Model model) {
        return "products.list";
    }

    @GetMapping("/view")
    public String view(Model model) {
        return "products.view";
    }

}
