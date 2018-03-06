<template>

<div id="app">
    <div class="container">
        <h1 class="left"> List of patients</h1>
        <button type="button" class="right addNewPatient" @click="showAddModal()">Add New Patient</button>
        <div class="clear">
        </div>
        <hr>
        <p class="errorMessage" v-if="errorMessage">{{errorMessage}}</p>
        <p class="successMessage" v-if="successMessage">{{successMessage}}</p>
        <table class="list">
            <tr>
                <th>ID</th>
                <th>FirstName</th>
                <th>LastName</th>
                <th>SSN</th>
                <th>MRN</th>
            </tr>
            <tr v-for="(patient, idx) in patients" :key="idx">
                <td>{{patient.id}}</td>
                <td>{{patient.firstName}}</td>
                <td>{{patient.lastName}}</td>
                <td>{{patient.ssn}}</td>
                <td>{{patient.altIds[0].value}}</td>
                <td>
                    <button @click="showingEditModal=true; selectPatient(patient);">Edit</button>
                </td>
                <td>
                    <button @click="showingDeleteModal=true;  selectPatient(patient);">Delete</button>
                </td>
            </tr>
        </table>
    </div>
    <!--edit patient modal -->
    <add-patient v-if="showingAddModal" :show.sync="showingAddModal"/>
    <!-- modal box-->
    <div id="editModal" v-if="showingEditModal">
        <div class="modalContainer">
            <div class="modalHeading">
                <h1 class="left">Edit this patient</h1>
                <button type="button" class="right close" @click="showingEditModal=false;">X</button>
                <div class="clear">
                </div>
            </div>
            <div class="modalContent">
                <div class="form">
                    <div class="form-group">
                        <label class="left" for="">FirstName:</label>
                        <input type="text" class="form-control" name="" v-model="clickedPatient.firstName">
                    </div>
                    <div class="form-group">
                        <label class="left" for="">LastName:</label>
                        <input type="text" class="form-control" name="" v-model="clickedPatient.lastName">
                    </div>
                    <div class="form-group">
                        <label class="left" for="">SSN:</label>
                        <input type="text" class="form-control" name="" v-model="clickedPatient.ssn">
                    </div>
                    <div class="form-group">
                        <label class="left" for="">MRN:</label>
                        <input type="text" class="form-control" name="" v-model="clickedPatient.altIds[0].value">
                    </div>
                    <div class="form-group">
                        <button class="btn right" @click="showingEditModal=false; updatePatient()">Update</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- modal box-->
    <div id="deleteModal" v-if="showingDeleteModal">
        <div class="modalContainer">
            <div class="modalHeading">
                <h1 class="left">Delete this patient</h1>
                <button type="button" class="right close" @click="showingDeleteModal=false;">X</button>
                <div class="clear">
                </div>
            </div>
            <div class="modalContent">
                <h4>You are about to delete {{clickedPatient.firstName}}</h4>
                <div class="clear">
                </div>
                <p>Are you sure you want to delete?</p>
                <br />
                <button class="danger" @click="showingDeleteModal=false; deletePatient()">YES</button>
                <button class="btn" @click="showingDeleteModal=false;">NO</button>
            </div>
        </div>
    </div>
</div>

</template>

<script>
import axios from "axios";
import { EventBus } from "./configuration/config";

import AddPatient from "./components/modals/AddPatient.vue";

export default {
  name: "app",
  data() {
    return {
      showingAddModal: false,
      showingEditModal: false,
      showingDeleteModal: false,
      errorMessage: "",
      successMessage: "",
      patients: [],
      newPatient: {
        firstName: "",
        lastName: "",
        ssn: "",
        altIds: [{ name: "mrn", value: "" }]
      },
      clickedPatient: {}
    };
  },
  components: {
    AddPatient
  },
  mounted: function() {
    this.getAllPatients();
  },
  beforeCreate() {
    // this.$options.components.AddPatient = require("./components/modals/AddPatient.vue").default;
  },
  methods: {
    showAddModal() {
      this.showingAddModal = true;
    },
    getAllPatients() {
      var self = this;
      axios
        .get("http://localhost:14606/api/v1/patient")
        .then(function(response) {
          if (response.data.error) {
            self.errorMessage = response.data.message;
          } else {
            console.log(
              "successfully received " + JSON.stringify(response.data)
            );
            self.patients = response.data;
          }
        });
    },
    savePatient(obj) {
      var self = this;
      console.log("saving:" + JSON.stringify(obj));
      axios
        .post("http://localhost:14606/api/v1/patient", obj)
        .then(function(response) {
          console.log(response);
          if (response.data.error) {
            self.errorMessage = response.data.message;
          } else {
            self.displaySuccess(
              "Successfully Saved " + response.data.firstName
            );
            self.getAllPatients();
          }
        })
        .catch(error => {
          console.log(error.response);
          self.errorMessage = error.response.data.message;
        });
    },
    updatePatient() {
      var self = this;

      console.log("updating:" + JSON.stringify(this.clickedPatient));
      axios
        .put(
          "http://localhost:14606/api/v1/patient/" + this.clickedPatient.id,
          this.clickedPatient
        )
        .then(function(response) {
          console.log(response);
          self.clickedPatient = {};
          if (response.data.error) {
            self.errorMessage = response.data.message;
          } else {
            self.displaySuccess(
              "Successfully Updated " + response.data.firstName
            );
            self.getAllPatients();
          }
        })
        .catch(error => {
          console.log(error.response);
          self.errorMessage = error.response.data.message;
        });
    },
    deletePatient() {
      var self = this;

      console.log("deleting:" + JSON.stringify(this.clickedPatient));
      axios
        .delete(
          "http://localhost:14606/api/v1/patient/" + this.clickedPatient.id
        )
        .then(function(response) {
          var context = self.clickedPatient.firstName;
          self.clickedPatient = {};
          if (response.data.error) {
            self.errorMessage = response.data.message;
          } else {
            self.displaySuccess("Successfully Deleted " + context);
            self.getAllPatients();
          }
        })
        .catch(error => {
          console.log(error.response);
          self.errorMessage = error.response.data.message;
        });
    },
    selectPatient(patient) {
      this.clickedPatient = patient;
    },
    clearMessage() {
      this.errorMessage = "";
      this.successMessage = "";
    },
    displaySuccess(message) {
      this.successMessage = message;
      setTimeout(() => {
        this.successMessage = "";
      }, 1000);
    }
  },
  created() {
    EventBus.$on("add:patient:close", () => {
      this.showingAddModal = false;
    });
    EventBus.$on("add:patient", o => {
      console.log("hi!");
      this.savePatient(o);
      this.showingAddModal = false;
    });
  }
};
</script>

<style scoped>
* {
  padding: 0px;
  margin: 0px;
  box-sizing: border-box;
}

.container {
  width: 960px;
  margin: auto;
  margin-top: 44px;
}

.left {
  float: left;
}

.right {
  float: right;
}

.clear {
  clear: both;
}

h1 {
  font-size: 32px;
}

.addNewPatient {
  float: 2px 9px;
}

table.list {
  width: 100%;
  text-align: center;
  margin-top: 33px;
}

table.list th {
  padding: 5px;
  background: #f41272;
  color: #fff;
}

table.list td {
  padding: 9px;
}

table.list tr {
  background: #d7dbdd;
}

button {
  padding: 10px 20px;
  border: 0px;
  background: #f2f3f4;
}

.modal {
  background: rgba(0, 0, 0, 0.4);
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}

.modalContainer {
  width: 555px;
  background: #fff;
  margin: 0 auto;
  margin-top: 44px;
}

.modalHeading {
  background: #f41272;
  padding: 6px;
  text-align: center;
  color: #fff;
}

.modalContent {
  min-height: 400px;
  padding: 44px;
}

.close {
  background: red;
  font-size: 32px;
  color: #fff;
  padding: 2px 9px;
  border: none;
}

label {
  font-family: "roboto", sans-serif;
  font-size: 15px;
}

.form {
  padding: 2px;
}

.form-group {
  padding: 20px;
}

.form-control {
  padding: 5px 6px;
  float: right;
  width: 80%;
  border-radius: 5px;
  border: 1px solid #d6dbdf;
}

.btn {
  background: #48c9b0;
  color: #fff;
  font-size: 14px;
  border-radius: 5px;
}

.danger {
  background: red;
  color: #fff;
  font-size: 14px;
  border-radius: 5px;
}

.successMessage {
  background: #d8efc2;
  color: #097133;
  border-left: 5px solid #097133;
  padding: 9px;
  margin: 22px 0;
}

.errorMessage {
  background: #efcbc2;
  color: #d71517;
  border-left: 5px solid #d71517;
  padding: 9px;
  margin: 22px 0;
}
</style>