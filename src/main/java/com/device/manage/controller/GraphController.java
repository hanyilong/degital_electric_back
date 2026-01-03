package com.device.manage.controller;

import com.device.manage.entity.Graph;
import com.device.manage.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graph")
@CrossOrigin(origins = "*")
public class GraphController {

    @Autowired
    private GraphService graphService;
    
    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping
    public List<Graph> getAllGraphs(@RequestParam(required = false) Long projectId) {
        return graphService.findAll(projectId);
    }

    @GetMapping("/{id}")
    public Graph getGraphById(@PathVariable Long id) {
        return graphService.findById(id);
    }

    @GetMapping("/search")
    public List<Graph> getGraphByIdAndName(@RequestParam Long projectId, @RequestParam String name) {
        return graphService.findByProjectIdAndName(projectId, name);
    }

    /**
     * 文件上传核心接口
     * @param file 前端上传的文件，参数名必须是 file ，对应Python request.files['file']
     * @return 统一JSON格式返回结果
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(MultipartFile file) {
        // 定义返回结果集，固定结构 code+msg+data
        Map<String, Object> result = new HashMap<>(3);

        // 1. 判空：如果没有上传文件，返回404+提示
        if (file == null || file.isEmpty()) {
            result.put("code", 404);
            result.put("msg", "file not found");
            result.put("data", "");
            return result;
        }

        try {
            // 2. 获取当前时间戳(秒级)
            long timestampInt = System.currentTimeMillis() / 1000;
            // 3. 获取文件原始名称，解析文件后缀
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            // 4. 拼接新文件名：时间戳.后缀名
            String fileName = timestampInt + "." + fileExtension;
            
            // 5. 创建上传目录（如果不存在）
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            // 6. 构建文件保存路径
            File destFile = new File(uploadDirectory, fileName);
            file.transferTo(destFile);
            
            // 7. 打印日志
            System.out.println("------------" + fileName + " saved to " + destFile.getAbsolutePath());

            // 8. 组装返回结果，预览地址
            String previewUrl = "http://localhost:8081/api/image/preview?filename=" + fileName;
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", previewUrl);

        } catch (IOException e) {
            // 异常兜底：文件保存失败时的返回
            result.put("code", 500);
            result.put("msg", "file upload failed: " + e.getMessage());
            result.put("data", "");
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 工具方法：获取文件后缀名 等价Python的 get_file_extension(file.filename)
     * @param filename 文件原始名称
     * @return 文件后缀（如：jpg、png、txt，不带点）
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        // 从最后一个点的位置截取后缀
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @PostMapping
    public boolean createGraph(@RequestBody Graph graph) {
        return graphService.save(graph);
    }

    @PutMapping("/{id}")
    public boolean updateGraph(@PathVariable Long id, @RequestBody Graph graph) {
        graph.setId(id);
        return graphService.update(graph);
    }

    @DeleteMapping("/{id}")
    public boolean deleteGraph(@PathVariable Long id) {
        return graphService.deleteById(id);
    }
}
