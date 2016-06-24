<div class="" data-ng-controller="${className}FormCtrl as formCtrl" data-ng-init="formCtrl.load()">
    <div class="page-title">
        <div class="title_left">
            <h3 data-ng-hide="formCtrl.person.id">New ${domainClass.naturalName}</h3>
            <h3 data-ng-show="formCtrl.person.id">Edit ${domainClass.naturalName}</h3>
        </div>
    </div>
    <div class="clearfix"></div>
    <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="x_panel">

                <div class="x_content">
                    <ul class="parsley-errors-list" data-ng-show="formCtrl.errorData.message">
                        <li>{{formCtrl.errorData.message}}</li>
                    </ul>
                    <ul class="parsley-errors-list" data-ng-show="formCtrl.errorData.total>1">
                        <li data-ng-repeat="error in formCtrl.errorData._embedded.errors">{{error.message}}</li>
                    </ul>
                    <form name="editForm"
                          class="form-horizontal form-label-left">
                        <% fields.each{field ->%>
                        <div class="form-group" data-ng-class="{bad:formCtrl.allErrorFields.indexOf('${field.property.name}')>-1}">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="${field.property.name}">${field.property.naturalName}
                               <% if(field.required){%> <span class="required">*</span> <% }%>
                            </label>

                            <div class="col-md-6 col-sm-6 col-xs-12">
                                ${field.renderEdit()}
                            </div>
                        </div>
                        <% } %>

                        <div class="form-group">
                            <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                                <br>
                                <button
                                        data-ng-show="formCtrl.${propertyName}.id"
                                        class="btn btn-danger"
                                        mwl-confirm
                                        title=" Are you sure?"
                                        message="Are you really <b>sure</b> you want to do this?"
                                        confirm-text="Yes <i class='glyphicon glyphicon-ok'></i>"
                                        cancel-text="No <i class='glyphicon glyphicon-remove'></i>"
                                        on-confirm="formCtrl.delete()"
                                        on-cancel="cancelClicked =true"
                                        confirm-button-type="danger"
                                        cancel-button-type="default">
                                    Delete
                                </button>
                                <a ui-sref="home.${propertyName}.list"
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