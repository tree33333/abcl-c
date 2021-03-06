package qsos.core.form.db.entity

/**
 * @author : 华清松
 * 表单项值-选项实体类
 * @param ckId 选项ID
 * @param ckName 选项名称
 * @param ckValue 选项值
 * @param ckChecked 是否被选
 */
data class FormValueOfCheck(
        var ckId: String? = null,
        var ckName: String? = null,
        var ckValue: String? = null,
        var ckChecked: Boolean = false
)