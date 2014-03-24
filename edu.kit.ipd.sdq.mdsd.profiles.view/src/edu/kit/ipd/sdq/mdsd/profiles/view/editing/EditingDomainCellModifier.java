package edu.kit.ipd.sdq.mdsd.profiles.view.editing;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;

public class EditingDomainCellModifier implements ICellModifier {

    private TransactionalEditingDomain editingDomain;

    private TableViewer tableViewer;

    public EditingDomainCellModifier(TransactionalEditingDomain editingDomain, TableViewer tableViewer) {
        this.editingDomain = editingDomain;
        this.tableViewer = tableViewer;
    }

    public void doModify(final Runnable runnable) {
        RecordingCommand cmd = new RecordingCommand(editingDomain) {

            @Override
            protected void doExecute() {
                runnable.run();
                if (tableViewer != null && !tableViewer.getTable().isDisposed())
                    tableViewer.refresh();
            }

        };
        editingDomain.getCommandStack().execute(cmd);
    }

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		// TODO Auto-generated method stub
		
	}

}