package com.example.securitymedianet.Services.Article;
import com.example.securitymedianet.Repositories.NotificationRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.example.securitymedianet.Entites.*;
import com.example.securitymedianet.Entites.Analyse.ArticleAnalysis;
import com.example.securitymedianet.Repositories.ArticleRepository;
import com.example.securitymedianet.Repositories.ProjectRepository;
import com.example.securitymedianet.Services.Flask.FlaskServices;
import com.example.securitymedianet.Services.Project.IProjectServices;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleService implements IArticleService{
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FlaskServices flaskServices;
    @Autowired
    private IProjectServices projectServices;
    @Override
    public void updateStatus(){
        List<Article> articles=articleRepository.findAll();
        for(Article article:articles){
            if (article.getRAF()!=0){
                article.setStatus(ArticleStatus.IN_PROGRESS);
            }else {
                article.setStatus(ArticleStatus.COMPLETED);
            }
            articleRepository.save(article);
        }
    }
    @Override
    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }
    @Override
    public Long countArticles(){
        return articleRepository.count();
    }
    @Override
    public Long countArticlesByStatus(ArticleStatus status){
        return articleRepository.countArticlesByStatus(status);
    }
    @Override
    public Map<String,Object> predictArticlesInprojectdrupal(Integer projectID){
        Map<String,Object> map=new HashMap<>();
        Project project = projectRepository.findById(projectID).get();
        List<Article> articles=articleRepository.findByStatusAndProject(ArticleStatus.IN_PROGRESS,project);

        logger.info("articles:",articles.size());
    Map<String,Object> map1=projectServices.getProjectArticlesConsumedHours(projectID);
        for(Article article:articles){
                if (article.getRessources().equals("Infographie ")){
                    Map<String,Object> prediction=flaskServices.predictInfographie(
                    ((Number) map1.get("Analyste concepteur")).floatValue(),
                    ((Number) map1.get("Insertion contenu")).floatValue());
                    map.put("Infographie ",((Number)prediction.get("prediction")).floatValue());
          //          map.put("Infographie ",map1.get("Infographie "));
                }
                if (article.getRessources().equals("Intégration")){

                    Map<String,Object> prediction=flaskServices.predictIntegration(
                            ((Number) map1.get("Ingénieur test")).floatValue(),
                            ((Number) map1.get("Ingénieur système")).floatValue(),
                            ((Number) map1.get("Infographie ")).floatValue());
                    map.put("Intégration",((Number)prediction.get("prediction")).floatValue());
          //          map.put("Intégration",map1.get("Intégration"));
                }
                if (article.getRessources().equals("Insertion contenu")){
                    Map<String,Object> prediction=flaskServices.predictIC(
                            ((Number) map1.get("Analyste concepteur")).floatValue(),
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Insertion contenu",((Number)prediction.get("prediction")).floatValue());
                   // map.put("Insertion contenu",map1.get("Insertion contenu"));
                }
                if (article.getRessources().equals("Ingénieur test")){
                    Map<String,Object> prediction=flaskServices.predictIT(
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue(),
                            ((Number) map1.get("Ingénieur système")).floatValue());
                    map.put("Ingénieur test",((Number)prediction.get("prediction")).floatValue());
      //              map.put("Ingénieur test",map1.get("Ingénieur test"));
                }
                if (article.getRessources().equals("Ingénieur système")){
                    Map<String,Object> prediction=flaskServices.predictIS(
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Ingénieur système",((Float)prediction.get("prediction")).floatValue());
          //          map.put("Ingénieur système",map1.get("Ingénieur système"));
                }
                if (article.getRessources().equals("Consultant SEO")){
                    Map<String,Object> prediction=flaskServices.predictSEO(
                            ((Number) map1.get("Analyste concepteur")).floatValue(),
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Consultant SEO",((Number)prediction.get("prediction")).floatValue());
      //              map.put("Consultant SEO",map1.get("Consultant SEO"));
                }
                if (article.getRessources().equals("Formation")){
                    Map<String,Object> prediction=flaskServices.predictFormation(
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Formation",((Number)prediction.get("prediction")).floatValue());
        //            map.put("Formation",map1.get("Formation"));
                }
        }

       if (map.isEmpty()){
           map.put("Nothing found",articles.size());
       }
        return map;
    }
    @Override
    public Map<String,Object> predictArticlesInAllprojectdrupal(){
    List<Project> projects=projectRepository.findByType("DRUPAL");
    Map<String,Object> map=new HashMap<>();
    for(Project project:projects){
        if (project.getStatus().equals(ProjectStatus.INPROGRESS)) {
            map.put(project.getName(), predictArticlesInprojectdrupal(project.getId()));
        }
    }
   return map;
    }
    @Override
    public List<Article> findByStatus(ArticleStatus status){
        return articleRepository.findByStatus(status);
    }
    @Override
    public Map<String,Object> findByInprogressDrupal(){
       List<Project> projects=projectRepository.findByTypeAndStatus("DRUPAL", ProjectStatus.INPROGRESS);
       Map<String,Object> map=new HashMap<>();
       for(Project project:projects){
           map.put(project.getName(),articlesInprojectdrupal(project.getId()));
       }
       return map;
    }
    public Map<String,Object> articlesInprojectdrupal(Integer projectID){
        Map<String,Object> map=new HashMap<>();
        Project project = projectRepository.findById(projectID).get();
        List<Article> articles=articleRepository.findByStatusAndProject(ArticleStatus.IN_PROGRESS,project);


        Map<String,Object> map1=projectServices.getProjectArticlesConsumedHours(projectID);
        for(Article article:articles){
            if (article.getRessources().equals("Infographie ")){

                map.put("Infographie ",map1.get("Infographie "));
            }
            if (article.getRessources().equals("Intégration")){


                map.put("Intégration",map1.get("Intégration"));
            }
            if (article.getRessources().equals("Insertion contenu")){

                map.put("Insertion contenu",map1.get("Insertion contenu"));
            }
            if (article.getRessources().equals("Ingénieur test")){

                map.put("Ingénieur test",map1.get("Ingénieur test"));
            }
            if (article.getRessources().equals("Ingénieur système")){

                map.put("Ingénieur système",map1.get("Ingénieur système"));
            }
            if (article.getRessources().equals("Consultant SEO")){

                map.put("Consultant SEO",map1.get("Consultant SEO"));
            }
            if (article.getRessources().equals("Formation")){

                map.put("Formation",map1.get("Formation"));
            }
        }

        if (map.isEmpty()){
            map.put("Nothing found",articles.size());
        }
        return map;
    }
    public Map<String,Object> articleTracking(Integer articleID){

        Map<String,Object> map1=new HashMap<>();
        Article article=articleRepository.findById(articleID).orElse(null);
        map1.put("Les jours consommées",article.getConsommés_J());
        map1.put("Le RAF",article.getRAF());

        return map1;
    }
    @Override
    public Map<String,Object> articleTrackingInproject(){
        Map<String,Object> map=new HashMap<>();
        List<Project> projects=projectRepository.findByTypeAndStatus("DRUPAL", ProjectStatus.INPROGRESS);
        for(Project project:projects){
        List<Article> articles=articleRepository.findByStatusAndProject(ArticleStatus.IN_PROGRESS,project);
            Map<String,Object> map1=new HashMap<>();
        for(Article article:articles){
            map1.put(article.getRessources(),articleTracking(article.getId()));

        }
      map.put(project.getName(),map1);
        }
        return map;
    }
    @Override
    public Article findByTitleAndProject(String title, Integer projectID){
        Project project = projectRepository.findById(projectID).orElse(null);
        return articleRepository.findByRessourcesAndProject(title,project);
    }
    @Override
    public Map<String,Object> predict(Integer articleID) {
        Article article=articleRepository.findById(articleID).orElse(null);
        Map<String,Object> map=new HashMap<>();
       Project project=article.getProject();

            if (article.getRessources().equals("Infographie ")) {
                float analysteConcepteur = findByTitleAndProject("Analyste concepteur", project.getId()).getAttérissage();
                float insertion_contenu = findByTitleAndProject("Insertion contenu", project.getId()).getAttérissage();
                 map = flaskServices.predictInfographie(analysteConcepteur, insertion_contenu);

            }
            if (article.getRessources().equals("Intégration")){

                float ingenieur_test=findByTitleAndProject("Ingénieur test", project.getId()).getAttérissage();
                float ingenieur_systeme=findByTitleAndProject("Ingénieur système", project.getId()).getAttérissage();
                float infographie=findByTitleAndProject("Infographie ", project.getId()).getAttérissage();
                 map = flaskServices.predictIntegration(ingenieur_test,ingenieur_systeme,infographie);

            }
            if (article.getRessources().equals("Insertion contenu")){
                float analysteConcepteur=findByTitleAndProject("Analyste concepteur", project.getId()).getAttérissage();
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                map = flaskServices.predictIC(analysteConcepteur,gestionCoordination);

            }
            if (article.getRessources().equals("Ingénieur test")){
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                float ingenieur_systeme=findByTitleAndProject("Ingénieur système", project.getId()).getAttérissage();
               map = flaskServices.predictIT(gestionCoordination,ingenieur_systeme);

            }
            if (article.getRessources().equals("Ingénieur système")){
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                map = flaskServices.predictIS(gestionCoordination);

            }
            if (article.getRessources().equals("Consultant SEO")){
                float analysteConcepteur=findByTitleAndProject("Analyste concepteur", project.getId()).getAttérissage();
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
               map = flaskServices.predictSEO(analysteConcepteur,gestionCoordination);

            }
            if (article.getRessources().equals("Formation")){
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                map = flaskServices.predictFormation(gestionCoordination);
            }

return map;

    }

    @Override
    public void exportHeaders(HttpServletResponse response) throws IOException {
        String[] headers = {"Projet", "Chef de projet", "Type de projet", "Date début", "Date fin ", "Ressources", "J/H Vendus", "Coût unitaire", "Consommés(H)", "Consommés(J)", "J/H Restant", "RAF", "Budget Additionnel ", "Attérissage", "Marge", "Budget", "Couts", "Marge en montant", "Marge en %"};

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Articles");

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=headers.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @Override
    public ResponseEntity<?> uploadFile(@org.jetbrains.annotations.NotNull MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please upload a file!", HttpStatus.BAD_REQUEST);
        }
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            String[] expectedHeaders = {"Projet", "Chef de projet", "Type de projet", "Date début", "Date fin ", "Ressources", "J/H Vendus", "Coût unitaire", "Consommés(H)", "Consommés(J)", "J/H Restant", "RAF", "Budget Additionnel ", "Attérissage", "Marge", "Budget", "Couts", "Marge en montant", "Marge en %"};

            Row firstRow = sheet.getRow(0);
            if (firstRow == null) {
                return new ResponseEntity<>("File does not contain headers!", HttpStatus.BAD_REQUEST);
            }

            for (int i = 0; i < expectedHeaders.length; i++) {
                Cell cell = firstRow.getCell(i);
                if (cell == null || !expectedHeaders[i].equals(getStringCellValue(cell))) {
                    logger.error("Header mismatch at column {}: Expected '{}', Found '{}'", i, expectedHeaders[i], getStringCellValue(cell));
                    return new ResponseEntity<>("Please upload a file that matches the structure!", HttpStatus.BAD_REQUEST);
                }
            }

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


            return ResponseEntity.ok("File uploaded and data saved!");

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to read the Excel file!", HttpStatus.INTERNAL_SERVER_ERROR);}
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
    @Override
    public String getProjectNamebyArticle(Integer articleID){
        Article article=articleRepository.findById(articleID).orElse(null);
        return article.getProject().getName();

    }
    @Override
    public List<Article> findByStatusAndProject(ArticleStatus status, Integer projectID) {
      Project project=  projectRepository.findById(projectID).orElse(null);
      return articleRepository.findByStatusAndProject(status, project);
    }
@    Override
    public List<ArticleAnalysis> findArticleSummaries() {
        return articleRepository.findArticleSummaries();
    }
    @Override
    @Transactional
    public void deleteAllData() {
        // Delete all articles and associated notifications
        notificationRepository.deleteAll();
        articleRepository.deleteAll();

        // Delete all projects
        projectRepository.deleteAll();
    }

}
