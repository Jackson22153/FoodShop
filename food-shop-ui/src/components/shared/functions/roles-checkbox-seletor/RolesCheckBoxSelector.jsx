import { FormControl, InputLabel, Select, MenuItem, Checkbox, ListItemText, OutlinedInput } from "@mui/material";
export default function RolesCheckBoxSelector({userRoles, handleChange, roles}){
    const ITEM_HEIGHT = 48;
    const ITEM_PADDING_TOP = 8;
    const MenuProps = {
        PaperProps: {
          style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
          },
        },
    };


    return(
        <FormControl sx={{ m: 1, width: 300 }}>
            <InputLabel id="role-checkbox-label">Roles</InputLabel>
            <Select
                labelId="role-checkbox-label"
                id="role-checkbox"
                multiple
                name="roles"
                value={userRoles}
                onChange={handleChange}
                input={<OutlinedInput label="Roles" />}
                renderValue={(selected) => selected.map((x) => x.roleName).join(', ')}
                MenuProps={MenuProps}
            >
            {roles.map((role) => (
                <MenuItem key={role.roleID} value={role}>
                    <Checkbox
                        checked={
                            userRoles.findIndex((userRole) => userRole.roleID === role.roleID) >= 0
                        }
                    />
                    <ListItemText primary={role.roleName} />
                </MenuItem>
            ))}
            </Select>
        </FormControl>
    )
}