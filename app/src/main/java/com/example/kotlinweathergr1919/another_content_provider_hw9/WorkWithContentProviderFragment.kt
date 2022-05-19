package com.example.kotlinweathergr1919.another_content_provider_hw9

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                for (i in permissions.indices) {
                    if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        getContacts()
                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                        explain()
                    }
                }
            }
            REQUEST_CODE_CALL -> {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.hand_permission),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun getContacts() {
        Toast.makeText(requireContext(), getString(R.string.getContacts), Toast.LENGTH_SHORT)
            .show()
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC" //вначале цифры потом буквы // от М к Б
        )
        cursor?.let {
            for (i in 0 until it.count) {
                if (cursor.moveToPosition(i)) {
                    val columnNameIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val name: String = cursor.getString(columnNameIndex)
                    val columnContactId =                                             //если писать через лишнюю переменную студия не ругается на курсор
                        cursor.getColumnIndex(ContactsContract.Contacts._ID)          //Value must be ≥ 0 but `getColumnIndex` can be -1
                    val contactId = cursor.getString(columnContactId)
                    val number = getNumberFromID(contentResolver, contactId)
                    addView(name, number)
                }
            }
        }
        cursor?.close()
    }

    private fun getNumberFromID(cr: ContentResolver, contactId: String): String {
        val phones = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
        )
        var number = "none"
        phones?.let { cursor ->
            while (cursor.moveToNext()) {
                val columnNumberId =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                number = cursor.getString(columnNumberId)
            }
        }
        phones?.close()
        return number
    }

    private fun addView(name: String, number: String) {
        binding.containerForContacts.addView(TextView(requireContext()).apply {
            text = getString(R.string.name_number, name, number)
            textSize = 30f
            setOnClickListener {
                makeCall(number)
            }
        })
    }

    private fun makeCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
            startActivity(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CODE_CALL)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()

        private const val REQUEST_CODE_PERMISSION = 999
        private const val REQUEST_CODE_CALL = 989

    }
}