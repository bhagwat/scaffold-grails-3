<div class="" data-ng-controller="${className}FormCtrl as formCtrl" data-ng-init="formCtrl.load()">
    <div class="page-title">
        <div class="title_left">
            <h3 data-ng-hide="formCtrl.${propertyName}.id">New ${domainClass.naturalName}</h3>
            <h3 data-ng-show="formCtrl.${propertyName}.id">Edit ${domainClass.naturalName}</h3>
        </div>
    </div>
    <div class="clearfix"></div>
    <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="x_panel">

                <div class="x_content">
                    <form name="editForm" class="form-horizontal form-label-left">
                        <div class="form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12"></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <ul class="parsley-errors-list" data-ng-show="formCtrl.errorData.errors">
                                    <li data-ng-repeat="error in formCtrl.errorData.errors">{{error.message}}</li>
                                </ul>
                            </div>
                        </div>
                        <% fields.each{field ->%>
                        <div class="form-group" data-ng-class="{bad:formCtrl.allErrorFields.indexOf('${field.property.name}')>-1}">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="${field.property.name}">${field.property.naturalName}
                               <% if(field.required){%> <span class="required">*</span> <% }%>
                            </label>

                            <div class="col-md-6 col-sm-6 col-xs-12">
                                ${field.renderEdit()}
                                <div field-validation-errors="editForm.${field.property.name}"></div>
                            </div>
                        </div>
                        <% } %>

                        <div class="form-group">
                            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                <br>
                                <button
                                        data-ng-show="formCtrl.${propertyName}.id"
                                        class="btn btn-danger"
                                        data-mwl-confirm
                                        title=" Are you sure?"
                                        data-message="Are you really <b>sure</b> you want to do this?"
                                        data-confirm-text="Yes <i class='glyphicon glyphicon-ok'></i>"
                                        data-cancel-text="No <i class='glyphicon glyphicon-remove'></i>"
                                        data-on-confirm="formCtrl.delete()"
                                        data-on-cancel="cancelClicked =true"
                                        data-confirm-button-type="danger"
                                        data-cancel-button-type="default">
                                    Delete
                                </button>
                                <a data-ui-sref="home.${propertyName}.list"
                                   class="btn btn-primary"
                                   data-ng-show="editForm.\$dirty">Cancel</a>
                                <button type="submit"
                                        data-ng-click="editForm.\$valid && formCtrl.save${className}()"
                                        class="btn btn-success">
                                    <span data-ng-hide="formCtrl.person.id">Save</span>
                                    <span data-ng-show="formCtrl.person.id">Update</span>
                                </button>
                            </div>
                        </div>
                        <div class="ln_solid"></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>