package cn.elytra.no_infer

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*

const val ANNOTATION_NAME = "cn.elytra.no_infer.NoInfer"

fun isAnnotatedWithExplicitGeneric(method: PsiMethod): Boolean = method.getAnnotation(ANNOTATION_NAME) != null

class JavaExplicitGenericTypeInspection : AbstractBaseJavaLocalInspectionTool() {
    private fun hasExplicitTypeArguments(expression: PsiReferenceParameterList): Boolean = expression.typeParameterElements.isNotEmpty()

    private fun checkExplicitGenericType(
        expression: PsiMethodCallExpression,
        method: PsiMethod,
        holder: ProblemsHolder,
    ) {
        // ignore methods that doesn't need a generic type.
        val typeParameterList = method.typeParameterList
        if (typeParameterList?.typeParameters?.isEmpty() == true) {
            return
        }

        // check if there's explicit generic type
        val typeArgumentList = expression.typeArgumentList
        if (!hasExplicitTypeArguments(typeArgumentList)) {
            holder.registerProblem(
                expression,
                NoInferMessageBundle.message("no_infer.inspection"),
                ProblemHighlightType.WARNING,
                JavaAddExplicitGenericTypeFix(),
            )
        }
    }

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                val method = expression.resolveMethod()
                if (method != null && isAnnotatedWithExplicitGeneric(method)) {
                    checkExplicitGenericType(expression, method, holder)
                }
            }
        }
}
