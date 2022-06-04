package org.bizzdeskgroup.api.v1;


import io.swagger.annotations.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.bizzdeskgroup.Dto.SuccessResponse;
import org.bizzdeskgroup.Dtos.Query.*;
import org.bizzdeskgroup.Helpers.Excelis;
import org.bizzdeskgroup.Helpers.PdfGen;
import org.bizzdeskgroup.models.UserRoles;
import org.bizzdeskgroup.services.AssessmentService;
import org.bizzdeskgroup.services.PosTransactionService;
import org.bizzdeskgroup.services.TransactionService;
import org.bizzdeskgroup.services.impl.PosTransactionServiceImpl;
import org.bizzdeskgroup.services.impl.TransactionServiceImpl;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.joda.time.DateTime;
import org.apache.pdfbox.pdfparser.PDFParser;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

//import static jdk.nashorn.internal.objects.Global.print;
import static org.bizzdeskgroup.helper.UserTokenUtil.ExtractTokenUserMdaId;

@Path("download")
@Api(value = "Download Reports", description = "")
@RequestScoped
//@Produces("application/vnd.ms-excel")
@Produces("application/pdf")
@Consumes("application/json")
public class DownloadResource {

    @Inject
    JsonWebToken jwt;

    TransactionService Transaction = null;
    AssessmentService Assess = null;
    PosTransactionService PosTransaction = null;



    @GET
    @Path("/test")
//    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
//    @RolesAllowed({UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.ProjectReport})
    @ApiResponses(value = {
            @ApiResponse(
                    message = "Paged list of transactions",
                    code = 200,
                    response = AdminTransactionDto.class,
                    responseContainer = "List")
    })
    public Response GetTest() throws Exception {
//        try {
//            DoExcelOpt(Res);
//            File file = new File("FILE_PATH");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = today.toDate();
            Date startDate = today.minusMonths(10).toDate();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Transaction = new TransactionServiceImpl();
            List<AdminTransactionDto> Res = Transaction.AllTransactionsDownload("transactionDate", false, "", "", startDate, endDate);
        final byte[] dataz = Excelis.writeToExcel("All Transactions",Res);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PDDocument document = Loader.loadPDF(new File(String.valueOf(data)));
        PDDocument doc = new PDDocument();
        try{
//            document = Loader.loadPDF(data);

            PDPage page = new PDPage();
            doc.addPage( page );
//            PDFont font = new PDType1Font(FontName.HELVETICA_BOLD);
            PDFont font = new PDType1Font(PDType1Font.HELVETICA_BOLD.getCOSObject());

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page))
            {
                contentStream.beginText();
                contentStream.setFont( font, 12 );
                contentStream.newLineAtOffset(100, 700);
//                contentStream.showText("Go to Document->File Attachments to View Embedded Files");
                contentStream.showText("Go to Document->File Attachments to View Embedded Files");

                String text = "";
                for(int i=0; i<Res.size();i++)
                {
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(100, 700);
//                    if(sentence.isEmpty()) continue;
                    contentStream.showText(Res.get(i) + "\n");

                }
                contentStream.endText();
            }

            //embedded files are stored in a named tree
            PDEmbeddedFilesNameTreeNode efTree = new PDEmbeddedFilesNameTreeNode();

            //first create the file specification, which holds the embedded file
            PDComplexFileSpecification fs = new PDComplexFileSpecification();

            // use both methods for backwards, cross-platform and cross-language compatibility.
            fs.setFile( "Test.txt" );
            fs.setFileUnicode("Test.txt");

            //create a dummy file stream, this would probably normally be a FileInputStream
            byte[] data = "This is the contents of the embedded file".getBytes(StandardCharsets.ISO_8859_1);
            ByteArrayInputStream fakeFile = new ByteArrayInputStream(data);
            PDEmbeddedFile ef = new PDEmbeddedFile(doc, fakeFile );
            //now lets some of the optional parameters
            ef.setSubtype( "text/plain" );
            ef.setSize( data.length );
            ef.setCreationDate( new GregorianCalendar() );

            // use both methods for backwards, cross-platform and cross-language compatibility.
            fs.setEmbeddedFile( ef );
            fs.setEmbeddedFileUnicode(ef);
            fs.setFileDescription("Very interesting file");

            // create a new tree node and add the embedded file
            PDEmbeddedFilesNameTreeNode treeNode = new PDEmbeddedFilesNameTreeNode();
            treeNode.setNames( Collections.singletonMap( "My first attachment",  fs ) );
            // add the new node as kid to the root node
            List<PDEmbeddedFilesNameTreeNode> kids = new ArrayList<>();
            kids.add(treeNode);
            efTree.setKids(kids);
            // add the tree to the document catalog
            PDDocumentNameDictionary names = new PDDocumentNameDictionary( doc.getDocumentCatalog() );
            names.setEmbeddedFiles( efTree );
            doc.getDocumentCatalog().setNames( names );

            // show attachments panel in some viewers
            doc.getDocumentCatalog().setPageMode(PageMode.USE_ATTACHMENTS);
        } catch (Exception ez) {
            ez.printStackTrace();
            System.out.println(ez.getCause());
        }
        doc.setVersion(1233);
        doc.save(baos);
        doc.close();

//        try (PDDocument document = PDFParser.load(new File(filename)))
//        try (PDDocument document = Loader.loadPDF(new File(String.valueOf(data))))
//        {
//            // choose your printing method:
//            PdfGen.print(document);
//            //printWithAttributes(document);
//            //printWithDialog(document);
//            //printWithDialogAndAttributes(document);
//            //printWithPaper(document);
//        }
            return Response.ok(baos.toByteArray()).header("Content-Disposition",
                    "attachment; filename=All Transactions.pdf").build();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(e.getCause())
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(400)
//                    .type(MediaType.APPLICATION_JSON_TYPE)
//                    .entity(e.getCause())
//                    .build();
//        }
    }

    @GET
    @Path("/transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin1, UserRoles.SubAdmin2, UserRoles.Admin, UserRoles.ProjectReport})
    public Response GetTransaction(@QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId,
                                   @QueryParam("apply_filter")boolean applyFilter, @QueryParam("filter_by")String filterBy,
                                   @QueryParam("filter")String filterValue, @QueryParam("start_date")String formStartDate,
                                   @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {
            String sortBy = "transactionDate";
            String[] userMda = ExtractTokenUserMdaId(jwt);
            Transaction = new TransactionServiceImpl();
            List<TransactionDto> Res = null;
            List<AdminTransactionDto> ResAdmin = null;

//            System.out.println(formStartDate);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

//            System.out.println(endDate);
            byte[] data = null;

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if (formMdaId != null && formMdaId > 0) {
                            Res = Transaction.AllMdaTransactionsDownload(formMdaId, formProjectId, sortBy, applyFilter, filterBy, filterValue, startDate, endDate);
                            data = Excelis.writeToExcel("Transactions",Res);
                        } else {
                            ResAdmin = Transaction.AllProjectTransactionsDownload(formProjectId, sortBy, applyFilter, filterBy, filterValue, startDate, endDate);
                            data = Excelis.writeToExcel("Transactions",ResAdmin);
                        }
                    } else {
                        ResAdmin= Transaction.AllTransactionsDownload(sortBy, applyFilter, filterBy, filterValue, startDate, endDate);
                        data = Excelis.writeToExcel("Transactions",ResAdmin);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    int ProjId = Integer.parseInt(userMda[2]);
                    if (formMdaId != null && formMdaId > 0) {
                        Res = Transaction.AllMdaTransactionsDownload(formMdaId, ProjId, sortBy, applyFilter, filterBy, filterValue, startDate, endDate);
                        data = Excelis.writeToExcel("Transactions",Res);
                    } else {
                        ResAdmin = Transaction.AllProjectTransactionsDownload(ProjId, sortBy, applyFilter, filterBy, filterValue, startDate, endDate);
                        data = Excelis.writeToExcel("Transactions",ResAdmin);
                    }
                    break;
                case UserRoles.SubAdmin2:
                    int MdaId = Integer.parseInt(userMda[1]);
                    int MdaProjId = Integer.parseInt(userMda[2]);
                    Res = Transaction.AllMdaTransactionsDownload(MdaId, MdaProjId, sortBy, applyFilter, filterBy, filterValue, startDate, endDate);
                    data = Excelis.writeToExcel("Transactions",Res);
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    int AgentMdaId = Integer.parseInt(userMda[1]);
//                    int UserId = Integer.parseInt(userMda[0]);
//                    Assess = new AssessmentServiceImpl();
//                    Res = Assess.PaidInvoice(sortBy, pageNo, pageSize, UserId);
//                    data = Excelis.writeToExcel("Transactions",Res);
//                    break;
            }
//            final byte[] data = Excelis.writeToExcel("Transactions",Res);
            return Response.ok(data).header("Content-Disposition",
                    "attachment; filename=Transactions.xlsx").build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setMessage("Bad Request");
            success.setData(e.getMessage());
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }



    @GET
    @Path("/pos/transaction")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    public Response GetPosTransaction(@QueryParam("Project_Id")Integer formProjectId,
                                      @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {
            String sortBy = "transactionDate";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaPosTransactions(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
            List<PosTransactionDto> Res = new LinkedList<PosTransactionDto>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            Res = PosTransaction.MdaPosTransactionsDownload(formMdaId, sortBy, startDate, endDate);

                        } else {
                            Res = PosTransaction.ProjectPosTransactionsDownload(formProjectId, sortBy, startDate, endDate);

                        }

                    } else {
                        Res = PosTransaction.AllPosTransactionsDownload(sortBy, startDate, endDate);
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        Res = PosTransaction.MdaPosTransactionsDownload(formMdaId, sortBy, startDate, endDate);
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        Res = PosTransaction.ProjectPosTransactionsDownload(ProjId, sortBy, startDate, endDate);
                    }
                    break;
                case UserRoles.SubAdmin2:
                    Res = PosTransaction.MdaPosTransactionsDownload(Integer.parseInt(userMda[1]), sortBy, startDate, endDate);
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
            }
//
//            success.setStatus(200);
//            success.setData(Res);
//            return Response.ok(success).build();
            byte[] data = Excelis.writeToExcel("POS_Transactions",Res);
            return Response.ok(data).header("Content-Disposition",
                    "attachment; filename=POS_Transactions.xlsx").build();
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }


    @GET
    @Path("/pos/remittance")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    public Response GetRemittance(@QueryParam("Rem_Status")boolean remStatus, @QueryParam("apply_status_filter")boolean applyFilter,
                                  @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {

            String sortBy = "generatedDate";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaRemittance(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
            List<RemittanceDto> Res = new LinkedList<RemittanceDto>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus, applyFilter, startDate, endDate );

                        } else {
                            Res = PosTransaction.ProjectRemittanceDownload(formProjectId, sortBy, remStatus, applyFilter, startDate, endDate );

                        }

                    } else {
                        Res = PosTransaction.AllRemittanceDownload(sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus , applyFilter, startDate, endDate );
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        Res = PosTransaction.ProjectRemittanceDownload(ProjId, sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin2:
                    Res = PosTransaction.MdaRemittanceDownload(Integer.parseInt(userMda[1]), sortBy, remStatus, applyFilter, startDate, endDate );
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
            }
//            success.setStatus(200);
//            success.setData(Res);
//            return Response.ok(success).build();


            byte[] data = Excelis.writeToExcel("Remittances",Res);
            return Response.ok(data).header("Content-Disposition",
                    "attachment; filename=Remittances.xlsx").build();

        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }



    @GET
    @Path("/assessment")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin, UserRoles.Individual, UserRoles.NonIndividual})
    public Response GetAssessment(@QueryParam("Rem_Status")boolean remStatus, @QueryParam("apply_status_filter")boolean applyFilter,
                                  @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {

            String sortBy = "generatedDate";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaRemittance(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
            List<RemittanceDto> Res = new LinkedList<RemittanceDto>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus, applyFilter, startDate, endDate );

                        } else {
                            Res = PosTransaction.ProjectRemittanceDownload(formProjectId, sortBy, remStatus, applyFilter, startDate, endDate );

                        }

                    } else {
                        Res = PosTransaction.AllRemittanceDownload(sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus , applyFilter, startDate, endDate );
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        Res = PosTransaction.ProjectRemittanceDownload(ProjId, sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin2:
                    Res = PosTransaction.MdaRemittanceDownload(Integer.parseInt(userMda[1]), sortBy, remStatus, applyFilter, startDate, endDate );
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
            }
//            success.setStatus(200);
//            success.setData(Res);
//            return Response.ok(success).build();


            byte[] data = Excelis.writeToExcel("Remittances",Res);
            return Response.ok(data).header("Content-Disposition",
                    "attachment; filename=Remittances.xlsx").build();

        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }


    @GET
    @Path("/enumeration")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin, UserRoles.NonIndividual, UserRoles.Individual})
    public Response GetEnumeration(@QueryParam("Rem_Status")boolean remStatus, @QueryParam("apply_status_filter")boolean applyFilter,
                                  @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {

            String sortBy = "generatedDate";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaRemittance(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
            List<RemittanceDto> Res = new LinkedList<RemittanceDto>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus, applyFilter, startDate, endDate );

                        } else {
                            Res = PosTransaction.ProjectRemittanceDownload(formProjectId, sortBy, remStatus, applyFilter, startDate, endDate );

                        }

                    } else {
                        Res = PosTransaction.AllRemittanceDownload(sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus , applyFilter, startDate, endDate );
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        Res = PosTransaction.ProjectRemittanceDownload(ProjId, sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin2:
                    Res = PosTransaction.MdaRemittanceDownload(Integer.parseInt(userMda[1]), sortBy, remStatus, applyFilter, startDate, endDate );
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
            }
//            success.setStatus(200);
//            success.setData(Res);
//            return Response.ok(success).build();


            byte[] data = Excelis.writeToExcel("Remittances",Res);
            return Response.ok(data).header("Content-Disposition",
                    "attachment; filename=Remittances.xlsx").build();

        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }



    @GET
    @Path("/poses")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    public Response GetPoses(@QueryParam("Rem_Status")boolean remStatus, @QueryParam("apply_status_filter")boolean applyFilter,
                                  @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {

            String sortBy = "generatedDate";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaRemittance(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
            List<RemittanceDto> Res = new LinkedList<RemittanceDto>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus, applyFilter, startDate, endDate );

                        } else {
                            Res = PosTransaction.ProjectRemittanceDownload(formProjectId, sortBy, remStatus, applyFilter, startDate, endDate );

                        }

                    } else {
                        Res = PosTransaction.AllRemittanceDownload(sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus , applyFilter, startDate, endDate );
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        Res = PosTransaction.ProjectRemittanceDownload(ProjId, sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin2:
                    Res = PosTransaction.MdaRemittanceDownload(Integer.parseInt(userMda[1]), sortBy, remStatus, applyFilter, startDate, endDate );
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
            }
//            success.setStatus(200);
//            success.setData(Res);
//            return Response.ok(success).build();


            byte[] data = Excelis.writeToExcel("Remittances",Res);
            return Response.ok(data).header("Content-Disposition",
                    "attachment; filename=Remittances.xlsx").build();

        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }


    @GET
    @Path("/users")
    @ApiOperation(value = " ", notes = "  ", authorizations = {@Authorization(value = "authorization")})
    @RolesAllowed({UserRoles.Agent, UserRoles.SubAdmin2, UserRoles.SubAdmin1, UserRoles.ProjectReport, UserRoles.Admin})
    public Response GetUsers(@QueryParam("Rem_Status")boolean remStatus, @QueryParam("apply_status_filter")boolean applyFilter,
                             @QueryParam("Project_Id")Integer formProjectId, @QueryParam("Mda_Id")Integer formMdaId, @QueryParam("start_date")String formStartDate, @QueryParam("end_date")String formEndDate){
        SuccessResponse success = new SuccessResponse();
        try {

            String sortBy = "generatedDate";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            DateTime today = new DateTime();
            Date endDate = formStartDate == null ? today.toDate() : formatter.parse(formEndDate);
            Date startDate = formEndDate == null ? today.minusMonths(3).toDate() : formatter.parse(formStartDate);

            String[] userMda = ExtractTokenUserMdaId(jwt);
            PosTransaction = new PosTransactionServiceImpl();
//            PaginatedDto Res = PosTransaction.MdaRemittance(Integer.parseInt(userMda[1]), sortBy, pageNo, pageSize);
            List<RemittanceDto> Res = new LinkedList<RemittanceDto>();

            Object[] roleCheck = jwt.getGroups().toArray();
            switch (roleCheck[0].toString()){
                case UserRoles.Admin:
                    if (formProjectId != null && formProjectId > 0) {
                        if(formMdaId != null && formMdaId > 0 ){
                            Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus, applyFilter, startDate, endDate );

                        } else {
                            Res = PosTransaction.ProjectRemittanceDownload(formProjectId, sortBy, remStatus, applyFilter, startDate, endDate );

                        }

                    } else {
                        Res = PosTransaction.AllRemittanceDownload(sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin1:
                case UserRoles.ProjectReport:
                    if (formMdaId != null && formMdaId > 0) {
                        Res = PosTransaction.MdaRemittanceDownload(formMdaId, sortBy, remStatus , applyFilter, startDate, endDate );
                    } else {
                        int ProjId = Integer.parseInt(userMda[2]);
                        Res = PosTransaction.ProjectRemittanceDownload(ProjId, sortBy, remStatus, applyFilter, startDate, endDate );
                    }
                    break;
                case UserRoles.SubAdmin2:
                    Res = PosTransaction.MdaRemittanceDownload(Integer.parseInt(userMda[1]), sortBy, remStatus, applyFilter, startDate, endDate );
                    break;
//                case UserRoles.Individual:
//                case UserRoles.NonIndividual:
////                    filterId = UserId;
//                    break;
            }
//            success.setStatus(200);
//            success.setData(Res);
//            return Response.ok(success).build();


            byte[] data = Excelis.writeToExcel("Remittances",Res);
            return Response.ok(data).header("Content-Disposition",
                    "attachment; filename=Remittances.xlsx").build();

        } catch (Exception e) {
            e.printStackTrace();
//            throw new BadRequestException(e);
            success.setStatus(400);
            success.setData(e.getMessage());
            success.setMessage("Bad Request");
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(success)
                    .build();
        }
    }
}
