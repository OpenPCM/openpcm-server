<template>
    <!-- modal box-->
    <div class="modal-mask" @click="close()" transition="modal">
        <div class="modal-container" @click.stop>
            <div class="modal-header">
                <h3>Add New Patient</h3>
            </div>
            <div class="modal-body">
                <div class="form">
                    <div class="form-group">
                        <label for="firstname">FirstName:</label>
                        <input id="firstname" type="text" class="form-control" name="" v-model="newPatient.firstName">
                    </div>
                    <div class="form-group">
                        <label for="lastname">LastName:</label>
                        <input id="lastname" type="text" class="form-control" name="" v-model="newPatient.lastName">
                    </div>
                    <div class="form-group">
                        <label class="left" for="">SSN:</label>
                        <input type="text" class="form-control" name="" v-model="newPatient.ssn">
                    </div>
                    <div class="form-group">
                        <label class="left" for="">MRN:</label>
                        <input type="text" class="form-control" name="" v-model="newPatient.altIds[0].value">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary right" @click="savePatient()">Save</button>
            </div>
        </div>
    </div>
</template>
<script>
import {EventBus} from '../../configuration/config';

export default {
    name: "add-patient",
    props: [],
    data() {
        return {
            newPatient: {
                altIds: [{ name: "mrn", value: "" }]
            }
        }
    },
    methods: {
                close() {
            EventBus.$emit('add:patient:close');
        },
        savePatient() {
            console.log('hey!');
            EventBus.$emit('add:patient', this.newPatient);
        }
    }
};
</script>

<style lang="scss">
select {
  width: 50px;
}
label {
    max-width: 49%;
}
input {
    max-width: 100%;
}
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
  padding: 30px;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-container {
  width: 300px;
  margin: 0px auto;
  padding: 10px 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 5px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
  font-family: Helvetica, Arial, sans-serif;
}

.modal-header h3 {
  margin-top: 0;
  color: #42b983;
}

.modal-body {
  margin: 20px 0;
}

.modal-default-button {
  float: right;
}

.calendar-input {
  color: #000;
  border: 1px solid #000;
  display: inline-block;
}

.form-title {
  width: 100px;
  display: inline-block;
  margin-right: 20px;
  font-weight: bold;
}

.service-type-input {
  width: 200px;
}

/*
 * The following styles are auto-applied to elements with
 * transition="modal" when their visibility is toggled
 * by Vue.js.
 *
 * You can easily play with the modal transition by editing
 * these styles.
 */

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
}

.modal-enter .modal-container,
.modal-leave-active .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>