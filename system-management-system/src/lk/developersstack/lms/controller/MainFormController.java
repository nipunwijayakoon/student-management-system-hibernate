package lk.developersstack.lms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.developersstack.lms.bo.BoFactory;
import lk.developersstack.lms.bo.custom.LaptopBo;
import lk.developersstack.lms.bo.custom.ProgramBo;
import lk.developersstack.lms.bo.custom.StudentBo;
import lk.developersstack.lms.dto.CreateLaptopDto;
import lk.developersstack.lms.dto.CustomRegistrationData;
import lk.developersstack.lms.dto.ProgramDto;
import lk.developersstack.lms.dto.StudentDto;
import lk.developersstack.lms.view.Tm.StudentTM;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

public class MainFormController {
    public TextField txtName;
    public TextField txtContact;
    public Button btnStudentSave;
    public TableView<StudentTM> tblStudents;
    public TableColumn colStudentId;
    public TableColumn colStudentName;
    public TableColumn colContactNumber;
    public TableColumn colSeeMore;
    public TableColumn colDelete;
    public TextField txtProgramTitle;
    public TextField txtProgramCredit;
    public TextField txtLapBrand;
    public TextField txtLapSearch;
    public TableView tblLaptops;
    public TableColumn colLapId;
    public TableColumn colBrand;
    public TableColumn colLapDelete;
    public ComboBox<Long> cmbStudent;
    public Button btnLaptopSave;
    public TextField txtLapSearch1;
    public TableView<CustomRegistrationData> tblRegistrations;
    public TableColumn colRegId;
    public TableColumn colDate;
    public TableColumn colStudent;
    public TableColumn colProgram;
    public ComboBox<Long> cmbStudentForProgram;
    public ComboBox<Long> cmbPrograms;

    private final StudentBo studentBo = BoFactory.getInstance().getBo(BoFactory.BoType.STUDENT);
    private final LaptopBo laptopBo = BoFactory.getInstance().getBo(BoFactory.BoType.LAPTOP);
    private final ProgramBo programBo = BoFactory.getInstance().getBo(BoFactory.BoType.PROGRAM);

    public void initialize() throws SQLException, ClassNotFoundException {

        colStudentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSeeMore.setCellValueFactory(new PropertyValueFactory<>("seeMoreBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("student"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("title"));

        loadAllStudents();
        loadAllStudentsForLaptopSection();
        loadProgramsForRegistrationSection();
        loadAllRegistrations();


        tblStudents.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedStudentTm = newValue;
                        txtName.setText(newValue.getName());
                        txtContact.setText(newValue.getContact());
                        btnStudentSave.setText("Update Student");
                    }
                });


    }

    private void loadAllStudentsForLaptopSection() throws SQLException, ClassNotFoundException {

        ObservableList<Long> obList = FXCollections.observableArrayList();

        for (StudentDto dto : studentBo.findAllStudent()
        ) {

            obList.add(dto.getId());

        }

        cmbStudent.setItems(obList);
        cmbStudentForProgram.setItems(obList);

    }

    private void loadProgramsForRegistrationSection() throws SQLException, ClassNotFoundException {

        ObservableList<Long> obList = FXCollections.observableArrayList(

                programBo.findAllStudentIds()
        );

        cmbPrograms.setItems(obList);

    }


    private StudentTM selectedStudentTm = null;

    private void loadAllStudents() throws SQLException, ClassNotFoundException {

        ObservableList<StudentTM> tmList = FXCollections.observableArrayList();

        for (StudentDto dto : studentBo.findAllStudent()
        ) {

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #c0392b");
            Button seeMorButton = new Button("See More");
            seeMorButton.setStyle("-fx-background-color: #2980b9");

            StudentTM tm = new StudentTM(dto.getId(), dto.getName(), dto.getContact(),
                    deleteButton, seeMorButton);
            tmList.add(tm);

            deleteButton.setOnAction(e -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> selectedButtonData = alert.showAndWait();
                if (selectedButtonData.get().equals(ButtonType.YES)) {

                    try {
                        studentBo.deleteStudentById(tm.getId());
                        new Alert(Alert.AlertType.INFORMATION, "Student Deleted").show();
                        loadAllStudents();
                    } catch (Exception exception) {
                        new Alert(Alert.AlertType.ERROR, "Try Again").show();
                    }


                }


            });

        }

        tblStudents.setItems(tmList);

    }

    public void btnSaveProgram(ActionEvent actionEvent) {

        try {
            programBo.saveProgram(
                    new ProgramDto(
                            txtProgramTitle.getText(),
                            Integer.parseInt(txtProgramCredit.getText())
                    )
            );
            new Alert(Alert.AlertType.INFORMATION, "Program Saved!").show();
            loadAllPrograms();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
        }
    }

    private void loadAllPrograms() {

        //to be implemented
    }

    public void newStudentOnAction(ActionEvent actionEvent) {

        selectedStudentTm = null;
        btnLaptopSave.setText("save Student");
    }

    public void btnSaveLaptopOnAction(ActionEvent actionEvent) {

        try {
            laptopBo.saveLaptop(
                    new CreateLaptopDto(
                            cmbStudent.getValue(),
                            txtLapBrand.getText()
                    )
            );
            new Alert(Alert.AlertType.INFORMATION, "Laptop Saved!").show();
            loadAllLaptops();
            ;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
        }
    }

    private void loadAllLaptops() {
        //to be implemented
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {

        try {
            programBo.register(
                    cmbStudentForProgram.getValue(),
                    cmbPrograms.getValue()
            );
            new Alert(Alert.AlertType.INFORMATION, "Registered!").show();
            loadAllRegistrations();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
        }


    }

    private void loadAllRegistrations() {
        ObservableList<CustomRegistrationData> list = FXCollections.observableArrayList(programBo.findAllRegistrations()
        );
//        for (CustomRegistrationData temp:programBo.findAllRegistrations()
//             ) {
//            System.out.println(temp);
//        }
        tblRegistrations.setItems(list);
    }

    public void btnSaveStudentOnAction(ActionEvent actionEvent) {

        StudentDto dto = new StudentDto();
        dto.setName(txtName.getText());
        dto.setContact(txtContact.getText());

        if (btnStudentSave.getText().equals("Update Student")) {

            if (selectedStudentTm == null) {
                new Alert(Alert.AlertType.ERROR, "Try again!").show();
                return;
            }

            try {
                dto.setId(selectedStudentTm.getId());
                studentBo.updateStudent(dto);
                new Alert(Alert.AlertType.INFORMATION, "Student Updated!").show();
                selectedStudentTm = null;
                btnStudentSave.setText("save Student");
                loadAllStudents();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Try Again").show();
            }


        } else {


            try {
                studentBo.saveStudent(dto);
                new Alert(Alert.AlertType.INFORMATION, "Student Saved!").show();
                loadAllStudents();
                loadAllStudentsForLaptopSection();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Try Again").show();
            }


        }


    }
}
