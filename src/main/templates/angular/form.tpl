<form>

<% fields.each{field -> %>
<div class="from-group">
    <div>
    <label>${field.property.naturalName}</label>
    <div>

    <div>
        ${field.render()}
    </div>
</div>
<% } %>
</form>