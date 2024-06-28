package com.example.securitymedianet.Controllers;

import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.ArticleStatus;
import com.example.securitymedianet.Entites.Project;
import com.example.securitymedianet.Repositories.ArticleRepository;
import com.example.securitymedianet.Repositories.ProjectRepository;
import com.example.securitymedianet.Services.Article.ArticleService;
import com.example.securitymedianet.Services.Project.IProjectServices;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ProjectRepository projectRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ArticleRepository articleRepository;
private final IProjectServices projectServices;
    private final ArticleService articleService;

    @GetMapping("/stpr/{id}/{status}")
  List<Article>  findByStatusAndProject(@PathVariable("status") ArticleStatus status,@PathVariable("id") Integer projectid) {
      Project project = projectRepository.findById(projectid).orElse(null);

      return articleRepository.findByStatusAndProject(status, project);
  }



    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please upload a file!", HttpStatus.BAD_REQUEST);
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null || row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                    return  ResponseEntity.ok("File uploaded and data saved!"); // Skip empty rows
                }

                Article article = new Article();
                article.setDate_début(getDateCellValue(row.getCell(3)));
                article.setDate_fin(getDateCellValue(row.getCell(4)));
                article.setRessources(getStringCellValue(row.getCell(5)));
                article.setJ_Vendus(getNumericCellValue(row.getCell(6)));
                article.setCoût_unitaire(getNumericCellValue(row.getCell(7)));
                article.setConsommés_H(getNumericCellValue(row.getCell(8)));
                article.setConsommés_J(getNumericCellValue(row.getCell(9)));
                article.setJ_Restant(getNumericCellValue(row.getCell(10)));
                article.setRAF(getNumericCellValue(row.getCell(11)));
                if (article.getRAF() == 0) {
                    article.setStatus(ArticleStatus.COMPLETED);
                } else {
                    article.setStatus(ArticleStatus.IN_PROGRESS);
                }
                article.setBudget_Additionnel(getNumericCellValue(row.getCell(12)));
                article.setAttérissage(getNumericCellValue(row.getCell(13)));
                article.setMarge(getNumericCellValue(row.getCell(14)));
                article.setBudget(getNumericCellValue(row.getCell(15)));
                article.setCouts(getNumericCellValue(row.getCell(16)));
                article.setMarge_en_montant(getNumericCellValue(row.getCell(17)));
                article.setMarge_en_percent(getNumericCellValue(row.getCell(18)));

                String projectName = getStringCellValue(row.getCell(0));
                Project project = projectRepository.findByName(projectName);
                if (project == null) {
                    project = new Project();
                    project.setName(projectName);
                    project.setChef_de_projet(getStringCellValue(row.getCell(1)));
                    project.setType(getStringCellValue(row.getCell(2)));
                    project.setDate_début(getDateCellValue(row.getCell(3)));
                    project.setDate_fin(getDateCellValue(row.getCell(4)));
                    article.setProject(project);
                    projectRepository.save(project);

                }
                article.setProject(project);
                articleRepository.save(article);
                List<Article> existingArticles = project.getArticles();
                if (existingArticles == null) {
                    existingArticles = new ArrayList<>();
                    project.setArticles(existingArticles);
                }

                boolean articleExists = false;
                for (Article existingArticle : existingArticles) {
                    if (existingArticle.getRessources().equals(article.getRessources())) {
                        existingArticles.remove(existingArticle);
                        articleRepository.delete(existingArticle);
                        articleExists = true;
                        break;
                    }
                }

                if (!articleExists) {
                    existingArticles.add(article);
                }
                projectRepository.save(project);
            }
            projectServices.checkStatus();
            return ResponseEntity.ok("File uploaded and data saved!");

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to read the Excel file!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private Date getDateCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getDateCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(cell.getStringCellValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    private float getNumericCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return 0;
        }
        return (float) cell.getNumericCellValue();
    }

    @PostMapping("/status")
    public void updateStatus(){
        articleService.updateStatus();
    }
    @GetMapping("/drupal/predictArticleinprogress/{id}")
    public Map<String,Object> predictArticlesInprojectdrupal(@PathVariable("id") Integer projectID){
      return   articleService.predictArticlesInprojectdrupal(projectID);
    }
    @GetMapping("/drupal/predictinprogress")
    public Map<String,Object> predictinprogress(){
        return articleService.predictArticlesInAllprojectdrupal();
    }
    @GetMapping("/drupal/inprogress")
    public Map<String,Object> inprogress(){
return articleService.findByInprogressDrupal();
    }
    @GetMapping("/tracking")
    public Map<String,Object> tracking(){
        return articleService.articleTrackingInproject();
    }
    @GetMapping("/to/{id}")
    public Article getByTitleAndProject(@RequestParam String ressources,@PathVariable("id")Integer id){
        return articleService.findByTitleAndProject(ressources, id);
    }
    @GetMapping("/drupal/predict/{id}")
    public Map<String,Object> predict(@PathVariable Integer id){
        return articleService.predict(id);

    }
}

