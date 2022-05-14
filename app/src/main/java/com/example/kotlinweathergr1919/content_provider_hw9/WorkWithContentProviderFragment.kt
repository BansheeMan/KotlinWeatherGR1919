package com.example.kotlinweathergr1919.content_provider_hw9

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentWorkWithContentProviderBinding

@Suppress("DEPRECATION")
class WorkWithContentProviderFragment : Fragment() {

    private var _binding: FragmentWorkWithContentProviderBinding? = null
    private val binding: FragmentWorkWithContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkWithContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                getContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                explain()
            }
            else -> {
                mRequestPermission()
            }
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.contacts_access))
            .setMessage(getString(R.string.very_need))
            .setPositiveButton(getString(R.string.get_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_PERMISSION)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getContacts() {
        Toast.makeText(requireContext(), getString(R.string.getContacts), Toast.LENGTH_SHORT).show()
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC" //вначале цифры потом буквы // от М к Б
        )
        cursor?.let {
            for (i in 0 until it.count){
                if(cursor.moveToPosition(i)){
                    val columnNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val name:String = cursor.getString(columnNameIndex)
                    binding.containerForContacts.addView(TextView(requireContext()).apply {
                        textSize = 30f
                        text= name
                    })
                }
            }
        }
        cursor?.close()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()

        private const val REQUEST_CODE_PERMISSION = 999
    }
}