package kz.project.gallery.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.viewbinding.library.fragment.viewBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentCreatePhotoBinding

class CreatePhotoFragment : Fragment() {

    private val binding: FragmentCreatePhotoBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO убрать
        binding.chipAddNewTag.setOnClickListener {
            if (!checkChipCount()) {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.chip_max_count),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            showBottomSheetDialog()
        }
    }

    /**
     * Если в chipGroup находится меньше 6 chip возвращает true
     */
    private fun checkChipCount() = binding.chipGroup.childCount < 6


    /**
     * Создаёт и показывает BottomSheetDialog для добавления нового тэга
     */
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = Dialog(requireContext())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_create_tag)

        val tagNameEditText = bottomSheetDialog.findViewById<EditText>(R.id.editTextTag)
        val tagNameInputLayout = bottomSheetDialog.findViewById<TextInputLayout>(R.id.textInputLayoutTag)
        val createTagButton = bottomSheetDialog.findViewById<Button>(R.id.createTagButton)

        createTagButton.setOnClickListener {
            val tagName = tagNameEditText.text.toString()

            if (tagName.isBlank()) {
                tagNameInputLayout.error = requireContext().getString(R.string.invalid_tag_name)
                return@setOnClickListener
            }

            tagNameEditText.error = null

            val chip = createChip(tagName)
            addChipToChipGroup(chip)

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(R.drawable.bottom_sheet_dialog_background)
            attributes.windowAnimations = R.style.BottomSheetAnimation
            setGravity(Gravity.BOTTOM)
        }

        bottomSheetDialog.show()
    }

    /**
     * Добавляет chip в chipGroup
     */
    private fun addChipToChipGroup(chip: Chip) {
        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
        }
        binding.chipGroup.addView(chip)
    }

    /**
     * Создает chip с назавнием тэга
     */
    private fun createChip(tagName: String) = Chip(requireContext()).apply {
        text = tagName
        isCloseIconVisible = true
        setChipBackgroundColorResource(R.color.mainPink)
        setTextColor(requireContext().getColor(R.color.white))
        setCloseIconTintResource(R.color.white)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
    }
}