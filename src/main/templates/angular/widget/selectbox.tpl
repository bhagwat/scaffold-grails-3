select(attributes){
    options.each{optionItem->
        fragment "option value: optionItem.id, optionItem.name", optionItem: optionItem
    }
}