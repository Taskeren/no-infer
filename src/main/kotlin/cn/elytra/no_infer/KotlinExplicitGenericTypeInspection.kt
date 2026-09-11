package cn.elytra.no_infer

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import org.jetbrains.kotlin.analysis.api.KaSession
import org.jetbrains.kotlin.idea.codeinsight.api.applicable.inspections.KotlinApplicableInspectionBase
import org.jetbrains.kotlin.idea.codeinsight.utils.getRenderedTypeArguments
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtVisitor
import org.jetbrains.kotlin.psi.KtVisitorVoid

class KotlinExplicitGenericTypeInspection : KotlinApplicableInspectionBase<KtCallExpression, String>() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): KtVisitor<*, *> =
        object : KtVisitorVoid() {
            override fun visitCallExpression(expression: KtCallExpression) {
                visitTargetElement(expression, holder, isOnTheFly)
            }
        }

    override fun KaSession.prepareContext(element: KtCallExpression): String? {
        if (element.typeArguments.isNotEmpty() || resolveNoInferFunction(element) == null) {
            return null
        }
        return getRenderedTypeArguments(element)?.takeIf { it.isNotEmpty() }
    }

    override fun InspectionManager.createProblemDescriptor(
        element: KtCallExpression,
        context: String,
        rangeInElement: TextRange?,
        onTheFly: Boolean,
    ): ProblemDescriptor =
        createProblemDescriptor(
            element,
            rangeInElement,
            NoInferMessageBundle.message("no_infer.inspection"),
            ProblemHighlightType.WARNING,
            onTheFly,
            KotlinAddExplicitGenericTypeFix(context),
        )
}
